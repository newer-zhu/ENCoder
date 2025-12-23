package com.holden.vocabulary.feign;

import com.holden.common.dto.AiVocabularyEnrichRequest;
import com.holden.common.dto.AiVocabularyResult;
import com.holden.vocabulary.config.AiVocabularyFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "ai-service",
        url = "${ai.service.url}",
        fallback = AiVocabularyFallback.class
)
public interface AiFeignClient {

    @PostMapping("/api/ai/enrich/batch")
    List<AiVocabularyResult> enrich(
            @RequestBody AiVocabularyEnrichRequest request
    );
}

