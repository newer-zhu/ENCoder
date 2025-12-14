package com.holden.vocabulary.controller;

import com.holden.vocabulary.entity.Vocabulary;
import com.holden.vocabulary.service.VocabularyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vocabulary")
public class VocabularyController {

    private final VocabularyService vocabularyService;

    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @PostMapping
    public String save(@RequestBody Vocabulary vocabulary) {
        vocabularyService.save(vocabulary);
        return "ok";
    }

    @GetMapping("/{id}")
    public Vocabulary get(@PathVariable Long id) {
        return vocabularyService.getById(id);
    }
}