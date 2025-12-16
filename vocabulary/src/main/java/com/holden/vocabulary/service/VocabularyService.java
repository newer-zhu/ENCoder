package com.holden.vocabulary.service;

import com.holden.vocabulary.entity.Vocabulary;
import com.holden.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;

    public Vocabulary save(Vocabulary incoming) {
        return vocabularyRepository.findByWord(incoming.getWord())
                .map(existing -> {
                    incoming.setId(existing.getId());
                    return vocabularyRepository.save(incoming);
                })
                .orElseGet(() -> vocabularyRepository.save(incoming));
    }


    public List<Vocabulary> findAll() {
        return vocabularyRepository.findAll();
    }

    public Optional<Vocabulary> findByWord(String word) {
        return vocabularyRepository.findByWord(word);
    }

    public boolean existsByWord(String word) {
        return vocabularyRepository.existsByWord(word);
    }

    /**
     * 查找尚未补充 tech context 的词汇
     */
    public List<Vocabulary> findWordsWithoutTechContext(int page, int size) {
        return vocabularyRepository.findWordsWithoutTechContext(
                PageRequest.of(page, size)
        );
    }
}
