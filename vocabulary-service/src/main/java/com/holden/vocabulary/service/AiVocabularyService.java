package com.holden.vocabulary.service;

import com.holden.common.dto.AiVocabularyEnrichRequest;
import com.holden.common.dto.AiVocabularyResult;
import com.holden.vocabulary.feign.AiFeignClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AiVocabularyService {
    private final AiFeignClient aiClient;

    @CircuitBreaker(name = "aiService", fallbackMethod = "fallback")
    @TimeLimiter(name = "aiService")
    @Bulkhead(name = "aiService", type = Bulkhead.Type.SEMAPHORE)
    public CompletableFuture<List<AiVocabularyResult>> enrichBatchAsync(
            AiVocabularyEnrichRequest request
    ) {
        return CompletableFuture.supplyAsync(
                () -> aiClient.enrich(request)
        );
    }

    private CompletableFuture<List<AiVocabularyResult>> fallback(
            AiVocabularyEnrichRequest request, Throwable t
    ) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
