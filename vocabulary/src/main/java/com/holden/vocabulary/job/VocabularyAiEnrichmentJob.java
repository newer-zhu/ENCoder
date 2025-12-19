package com.holden.vocabulary.job;

import com.holden.common.context.AiConversationContext;
import com.holden.common.dto.AiVocabularyEnrichRequest;
import com.holden.common.dto.AiVocabularyResult;
import com.holden.common.entity.Vocabulary;
import com.holden.common.entity.VocabularySentence;
import com.holden.common.entity.VocabularyTechContext;
import com.holden.vocabulary.feign.AiFeignClient;
import com.holden.vocabulary.repository.SysDictRepository;
import com.holden.vocabulary.service.VocabularySentenceService;
import com.holden.vocabulary.service.VocabularyService;
import com.holden.vocabulary.service.VocabularyTechContextService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class VocabularyAiEnrichmentJob {

    private final VocabularyService vocabularyService;
    private final VocabularyTechContextService techContextService;
    private final VocabularySentenceService sentenceService;
    private final AiFeignClient doubaoAiService;
    private final SysDictRepository sysDictRepo;
    private final EntityManager entityManager;

    private AiConversationContext ctx = new AiConversationContext();


    /**
     * 每 10 分钟执行一次：
     * - 为程序员词汇补充【软件行业解释 + 例句】
     * - 非软件行业词汇直接软删除
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void enrichVocabulary() {
        doJob();
    }

    @Transactional
    public void doJob(){
        String promptTemplate = sysDictRepo
                .findByDictTypeAndDictKey("AI_PROMPT", "VOCAB_TECH_PROMPT")
                .orElseThrow(() -> new IllegalStateException("AI_PROMPT:VOCAB_TECH_PROMPT 未配置"))
                .getDictValue();

        // 取前 N 个尚未补充 tech context 的词汇（你已有的 service 方法）
        List<Vocabulary> words = vocabularyService.findWordsWithoutTechContext(0, 20);
        List<String> wordList = words.stream()
                .map(Vocabulary::getWord)
                .collect(Collectors.toList());
        // 调用 AI
        List<AiVocabularyResult> vocabularyResults = doubaoAiService.enrich(
                new AiVocabularyEnrichRequest(promptTemplate, wordList, ctx));

        for (AiVocabularyResult result : vocabularyResults) {
            Vocabulary vocab = new Vocabulary();
            try {
                vocab.setWord(result.getWord());
                // 非软件行业 → 软删除
                if (!result.isSoftwareRelated()) {
                    vocab.setSoftDeleted(true);
                    vocabularyService.save(vocab);
                    log.info("词汇 '{}' 非软件行业，已软删除", vocab.getWord());
                    continue;
                }

                Long vocabId = vocabularyService.findByWord(vocab.getWord()).get().getId();
                //用 reference，而不是 new Vocabulary()
                Vocabulary vocabRef =
                        entityManager.getReference(Vocabulary.class, vocabId);

                // Tech Context（只取一条）
                if (result.getTechContexts() != null && !result.getTechContexts().isEmpty()) {
                    var tc = result.getTechContexts().get(0);
                    VocabularyTechContext techContext = new VocabularyTechContext(
                            vocabRef,
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
                            vocabRef,
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
