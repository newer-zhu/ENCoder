package com.holden.vocabulary.controller.test;

import com.holden.vocabulary.job.VocabularyAiEnrichmentJob;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/internal/job")
@RequiredArgsConstructor
@Profile("dev")
public class JobDebugController {

    private final VocabularyAiEnrichmentJob job;

    @PostMapping("/enrich")
    public String run() throws ExecutionException, InterruptedException {
        job.doJob();
        return "OK";
    }
}
