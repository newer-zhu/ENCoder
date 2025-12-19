package com.holden.vocabulary.service;


import com.holden.common.entity.Vocabulary;
import com.holden.common.entity.VocabularySentence;
import com.holden.vocabulary.repository.VocabularySentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/14 18:03
 */
@Service
@RequiredArgsConstructor
public class VocabularySentenceService {

    private final VocabularySentenceRepository repository;

    public VocabularySentence save(VocabularySentence sentence) {
        return repository.save(sentence);
    }

    public List<VocabularySentence> findByVocabulary(Vocabulary vocabulary) {
        return repository.findByVocabulary(vocabulary);
    }
}
