package com.holden.vocabulary.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holden.vocabulary.dto.AiVocabularyResult;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/14 17:58
 */
@Slf4j
@Service
public class DoubaoAiService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ark.api-key}")
    private String apiKey;

    @Value("${ark.base-url}")
    private String baseUrl;

    @Value("${ark.model}")
    private String model;

    private ArkService arkService;

    @PostConstruct
    public void init() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("ark.api-key 未配置");
        }

        this.arkService = ArkService.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();

        log.info("DoubaoAiService initialized, model={}", model);
    }

    public AiVocabularyResult enrich(String prompt, String word) {
        List<ChatMessage> messages = List.of(
                ChatMessage.builder()
                        .role(ChatMessageRole.USER)
                        .content(prompt)
                        .build()
        );

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .build();

        String rawContent = arkService.createChatCompletion(request)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent().toString();

        return parseSafely(rawContent, word);
    }

    /**
     * 容错 JSON 解析
     */
    private AiVocabularyResult parseSafely(String content, String word) {
        try {
            String json = extractJson(content);
            return objectMapper.readValue(json, AiVocabularyResult.class);
        } catch (Exception e) {
            log.error("AI JSON 解析失败, word={}, content={}", word, content);
            // 解析失败，视为非软件词，避免反复调用
            AiVocabularyResult fallback = new AiVocabularyResult();
            fallback.setSoftwareRelated(false);
            return fallback;
        }
    }

    /**
     * 从 AI 输出中提取 JSON（防止出现多余文本）
     */
    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start == -1 || end == -1 || start >= end) {
            throw new IllegalArgumentException("未找到合法 JSON");
        }
        return text.substring(start, end + 1);
    }
}

