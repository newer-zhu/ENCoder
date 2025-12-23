package com.holden.vocabulary.service;

import com.holden.common.entity.Vocabulary;
import com.holden.common.entity.VocabularyTechContext;
import com.holden.vocabulary.repository.VocabularyTechContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/14 18:03
 */
@Service
@RequiredArgsConstructor
public class VocabularyTechContextService {

    private final VocabularyTechContextRepository repository;

    public VocabularyTechContext save(VocabularyTechContext techContext) {
        return repository.save(techContext);
    }

    public List<VocabularyTechContext> findByVocabulary(Vocabulary vocabulary) {
        return repository.findByVocabulary(vocabulary);
    }

    public Optional<VocabularyTechContext> findByVocabularyAndDomain(Vocabulary vocabulary, String domain) {
        return repository.findByVocabularyAndDomain(vocabulary, domain);
    }

    public boolean exists(Vocabulary vocabulary, String domain) {
        return repository.existsByVocabularyAndDomain(vocabulary, domain);
    }
}
