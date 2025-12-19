package com.holden.vocabulary.repository;


import com.holden.common.entity.Vocabulary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/14 17:40
 */

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    Optional<Vocabulary> findByWord(String word);

    boolean existsByWord(String word);

    @Query("""
        SELECT v
        FROM Vocabulary v
        WHERE v.softDeleted = false
          AND NOT EXISTS (
              SELECT 1
              FROM VocabularyTechContext tc
              WHERE tc.vocabulary = v
          )
        ORDER BY v.id
    """)
    List<Vocabulary> findWordsWithoutTechContext(Pageable pageable);

}
