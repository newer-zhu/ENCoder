package com.holden.vocabulary.job;

import com.holden.vocabulary.dto.AiVocabularyResult;
import com.holden.vocabulary.entity.Vocabulary;
import com.holden.vocabulary.entity.VocabularySentence;
import com.holden.vocabulary.entity.VocabularyTechContext;
import com.holden.vocabulary.repository.SysDictRepository;
import com.holden.vocabulary.service.VocabularySentenceService;
import com.holden.vocabulary.service.VocabularyService;
import com.holden.vocabulary.service.VocabularyTechContextService;
import com.holden.vocabulary.service.ai.DoubaoAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VocabularyAiEnrichmentJob {

    private final VocabularyService vocabularyService;
    private final VocabularyTechContextService techContextService;
    private final VocabularySentenceService sentenceService;
    private final DoubaoAiService doubaoAiService;
    private final SysDictRepository sysDictRepo;

    /**
     * 每 10 分钟执行一次：
     * - 为程序员词汇补充【软件行业解释 + 例句】
     * - 非软件行业词汇直接软删除
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void enrichVocabulary() {
        doJob();
    }

    public void doJob(){
        String promptTemplate = sysDictRepo
                .findByDictTypeAndDictKey("AI_PROMPT", "VOCAB_TECH_PROMPT")
                .orElseThrow(() -> new IllegalStateException("AI_PROMPT:VOCAB_TECH_PROMPT 未配置"))
                .getDictValue();

        // 取前 N 个尚未补充 tech context 的词汇（你已有的 service 方法）
        List<Vocabulary> words = vocabularyService.findWordsWithoutTechContext(0, 50);

        for (Vocabulary vocab : words) {
            try {
                // 已有例句（>=1）则跳过，后续交给懒加载
                List<VocabularySentence> existingSentences =
                        sentenceService.findByVocabulary(vocab);
                if (!existingSentences.isEmpty()) {
                    log.debug("词汇 '{}' 已有例句，跳过", vocab.getWord());
                    continue;
                }

                // 构建 prompt（核心改动点）
                String prompt = promptTemplate.replace("{{word}}", vocab.getWord());

                // 调用 AI
                AiVocabularyResult result = doubaoAiService.enrich(prompt, vocab.getWord());

                if (result == null) {
                    log.warn("词汇 '{}' AI 返回为空，跳过本轮", vocab.getWord());
                    continue;
                }

                // 非软件行业 → 软删除
                if (!result.isSoftwareRelated()) {
                    vocab.setSoftDeleted(true);
                    vocabularyService.save(vocab);
                    log.info("词汇 '{}' 非软件行业，已软删除", vocab.getWord());
                    continue;
                }

                // Tech Context（只取一条）
                if (result.getTechContexts() != null && !result.getTechContexts().isEmpty()) {
                    var tc = result.getTechContexts().get(0);
                    VocabularyTechContext techContext = new VocabularyTechContext(
                            vocab,
                            tc.getDomain(),
                            tc.getTechTranslation(),
                            tc.getExplanation()
                    );
                    techContextService.save(techContext);
                }

                // 例句（只取一条）
                if (result.getSentences() != null && !result.getSentences().isEmpty()) {
                    var s = result.getSentences().get(0);
                    VocabularySentence sentence = new VocabularySentence(
                            vocab,
                            s.getSentence(),
                            s.getTranslation(),
                            s.getSource()
                    );
                    sentenceService.save(sentence);
                }

                log.info("词汇 '{}' AI 补全完成", vocab.getWord());

            } catch (Exception e) {
                // 不影响下一条词
                log.error("AI 补全词汇 '{}' 失败", vocab.getWord(), e);
            }
        }
    }
}
