package com.holden.vocabulary.dto;

import lombok.Data;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/14 18:25
 */
@Data
public class SentenceDTO {

    private String sentence;
    private String translation;

    public String getSource() {
        return "ai";
    }
}
