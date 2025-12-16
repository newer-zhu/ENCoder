package com.holden.vocabulary.service.ai;

import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.Collections;
import java.util.List;

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

    /**
     * 批量 enrich（推荐使用）
     */
    public List<AiVocabularyResult> enrichBatch(String prompt, List<String> wordList) {
        if (wordList == null || wordList.isEmpty()) {
            return Collections.emptyList();
        }

        String finalPrompt = buildPrompt(prompt, wordList);

        List<ChatMessage> messages = List.of(
                ChatMessage.builder()
                        .role(ChatMessageRole.USER)
                        .content(finalPrompt)
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
                .getContent()
                .toString();

        return parseBatchSafely(rawContent, wordList);
    }

    /**
     * 构建最终 prompt（prompt + wordList）
     */
    private String buildPrompt(String prompt, List<String> wordList) {
        try {
            String wordsJson = objectMapper.writeValueAsString(wordList);
            return prompt + "\n\n" + wordsJson;
        } catch (Exception e) {
            throw new IllegalStateException("构建 wordList JSON 失败", e);
        }
    }

    /**
     * 容错解析 JSON 数组
     */
    private List<AiVocabularyResult> parseBatchSafely(String content, List<String> wordList) {
        try {
            String jsonArray = extractJsonArray(content);
            return objectMapper.readValue(
                    jsonArray,
                    new TypeReference<List<AiVocabularyResult>>() {}
            );
        } catch (Exception e) {
            log.error("AI JSON 数组解析失败, words={}, content={}", wordList, content);

            // 兜底：全部视为非软件词，避免重试风暴
            return wordList.stream().map(w -> {
                AiVocabularyResult r = new AiVocabularyResult();
                r.setWord(w);
                r.setSoftwareRelated(false);
                return r;
            }).toList();
        }
    }

    /**
     * 从 AI 输出中提取 JSON 数组
     */
    private String extractJsonArray(String text) {
        int start = text.indexOf("[");
        int end = text.lastIndexOf("]");
        if (start == -1 || end == -1 || start >= end) {
            throw new IllegalArgumentException("未找到合法 JSON 数组");
        }
        return text.substring(start, end + 1);
    }
}
