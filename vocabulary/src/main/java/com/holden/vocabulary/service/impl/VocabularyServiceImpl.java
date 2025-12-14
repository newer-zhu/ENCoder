package com.holden.vocabulary.service.impl;

import com.holden.vocabulary.entity.Vocabulary;
import com.holden.vocabulary.mapper.VocabularyMapper;
import com.holden.vocabulary.service.VocabularyService;
import org.springframework.stereotype.Service;

@Service
public class VocabularyServiceImpl implements VocabularyService {

    private final VocabularyMapper vocabularyMapper;

    public VocabularyServiceImpl(VocabularyMapper vocabularyMapper) {
        this.vocabularyMapper = vocabularyMapper;
    }

    @Override
    public Vocabulary getById(Long id) {
        return vocabularyMapper.selectById(id);
    }

    @Override
    public void save(Vocabulary vocabulary) {
        vocabularyMapper.insert(vocabulary);
    }
}