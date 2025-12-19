package com.holden.vocabulary.controller;

import com.holden.common.entity.Vocabulary;
import com.holden.vocabulary.service.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vocabularies")
@RequiredArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;

    /**
     * 查询所有词汇
     */
    @GetMapping
    public List<Vocabulary> listAll() {
        return vocabularyService.findAll();
    }

    /**
     * 根据单词查询
     */
    @GetMapping("/{word}")
    public Vocabulary getByWord(@PathVariable String word) {
        return vocabularyService.findByWord(word)
                .orElseThrow(() -> new RuntimeException("Word not found: " + word));
    }

    /**
     * 新增单词（手动添加 / 测试用）
     */
    @PostMapping
    public Vocabulary create(@RequestBody Vocabulary vocabulary) {
        if (vocabularyService.existsByWord(vocabulary.getWord())) {
            throw new RuntimeException("Word already exists: " + vocabulary.getWord());
        }
        return vocabularyService.save(vocabulary);
    }
}
