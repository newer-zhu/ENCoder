package com.holden.vocabulary.repository;

import com.holden.vocabulary.entity.Vocabulary;
import com.holden.vocabulary.entity.VocabularyTechContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VocabularyTechContextRepository extends JpaRepository<VocabularyTechContext, Long> {

    List<VocabularyTechContext> findByVocabulary(Vocabulary vocabulary);

    Optional<VocabularyTechContext> findByVocabularyAndDomain(Vocabulary vocabulary, String domain);

    boolean existsByVocabularyAndDomain(Vocabulary vocabulary, String domain);
}
