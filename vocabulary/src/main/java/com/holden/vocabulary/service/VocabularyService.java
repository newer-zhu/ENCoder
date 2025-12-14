package com.holden.vocabulary.service;

import com.holden.vocabulary.entity.Vocabulary;

public interface VocabularyService {
    Vocabulary getById(Long id);
    void save(Vocabulary vocabulary);
}
