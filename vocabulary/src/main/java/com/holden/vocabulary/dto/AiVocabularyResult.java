package com.holden.vocabulary.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/14 18:23
 */
@Data
public class AiVocabularyResult {

    private Boolean softwareRelated;

    private List<TechContextDTO> techContexts;

    private List<SentenceDTO> sentences;
    private String word;

    public boolean isSoftwareRelated() {
        return Boolean.TRUE.equals(softwareRelated);
    }
}
