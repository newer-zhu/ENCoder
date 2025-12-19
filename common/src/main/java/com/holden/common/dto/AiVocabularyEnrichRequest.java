package com.holden.common.dto;

import com.holden.common.context.AiConversationContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/19 22:10
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class AiVocabularyEnrichRequest {

    /**
     * prompt 模板（可选，没传就用默认）
     */
    private String prompt;

    /**
     * 词汇列表
     */
    private List<String> words;

    /**
     * 会话上下文（可选）
     */
    private AiConversationContext context;
}
