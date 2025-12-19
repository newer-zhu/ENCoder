package com.holden.ai.controller;

import com.holden.ai.service.DoubaoAiService;
import com.holden.common.context.AiConversationContext;
import com.holden.common.dto.AiVocabularyEnrichRequest;
import com.holden.common.dto.AiVocabularyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIVocabularyController {

    private final DoubaoAiService doubaoAiService;

    /**
     * 查询所有词汇
     */
    /**
     * 批量补全词汇（软件工程语境）
     */
    @PostMapping("/enrich/batch")
    public List<AiVocabularyResult> enrichBatch(
            @RequestBody AiVocabularyEnrichRequest request) {

        if (request.getWords() == null || request.getWords().isEmpty()) {
            throw new IllegalArgumentException("word list must not be empty");
        }

        AiConversationContext context = request.getContext();
        String prompt = request.getPrompt();

        log.info("AI enrich batch, size={}", request.getWords().size());

        return doubaoAiService.enrichBatch(
                context,
                prompt,
                request.getWords()
        );
    }


}
