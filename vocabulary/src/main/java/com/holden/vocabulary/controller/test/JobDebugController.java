package com.holden.vocabulary.controller.test;

import com.holden.vocabulary.job.VocabularyAiEnrichmentJob;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/job")
@RequiredArgsConstructor
@Profile("dev")
public class JobDebugController {

    private final VocabularyAiEnrichmentJob job;

    @PostMapping("/enrich")
    public String run() {
        job.doJob();
        return "OK";
    }
}
