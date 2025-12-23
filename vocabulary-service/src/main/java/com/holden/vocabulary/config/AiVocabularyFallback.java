package com.holden.vocabulary.config;

import com.holden.common.dto.AiVocabularyEnrichRequest;
import com.holden.common.dto.AiVocabularyResult;
import com.holden.vocabulary.feign.AiFeignClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/21 16:51
 */
@Slf4j
public class AiVocabularyFallback implements AiFeignClient {
    @Override
    public List<AiVocabularyResult> enrich(AiVocabularyEnrichRequest request) {
        log.warn("AI service fallback triggered, words={}", request.getWords());
        return Collections.emptyList();
    }
}
