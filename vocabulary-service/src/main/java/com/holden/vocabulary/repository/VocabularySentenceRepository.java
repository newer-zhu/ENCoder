package com.holden.vocabulary.repository;

import com.holden.common.entity.Vocabulary;
import com.holden.common.entity.VocabularySentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/14 18:02
 */
public interface VocabularySentenceRepository extends JpaRepository<VocabularySentence, Long> {

    List<VocabularySentence> findByVocabulary(Vocabulary vocabulary);

}
