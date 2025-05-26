package com.balanceeat.demo.domain.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpenAIService {
    private final ChatClient chatClient;
    
    /**
     * ChatClient.Builder와 PromptChatMemoryAdvisor는 Spring AI가 자동 등록합니다.
     * 기본 Advisor로 메모리 어댑터를 넣고 빌드합니다.
     */
    public OpenAIService(ChatClient.Builder chatClientBuilder,
        PromptChatMemoryAdvisor memoryAdvisor) {
        this.chatClient = chatClientBuilder
            .defaultAdvisors(memoryAdvisor)   // 메모리 어댑터 기본 적용
            .build();
        log.info("OpenAIService initialized with ChatClient and MemoryAdvisor");
    }
    
    /**
     * @param sessionId  세션(대화) 식별자 – UUID·JWT·쿠키·헤더 등 호출부에서 전달
     * @param userPrompt 사용자의 입력 메시지
     * @return           ChatGPT가 생성한 응답
     */
    public String getAssistantReply(String sessionId, String userPrompt) {
        log.debug("Received chat request - SessionId: {}, UserPrompt: {}", sessionId, userPrompt);
        try {
            String response = chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .user(userPrompt)
                .call()
                .content();
            log.debug("Chat response received - SessionId: {}, Response: {}", sessionId, response);
            return response;
        } catch (Exception e) {
            log.error("Error while calling OpenAI API - SessionId: {}, Error: {}", sessionId, e.getMessage(), e);
            throw e;
        }
    }

    /* ---------------- 선택: 스트리밍 응답 ----------------
    public Flux<ChatResponseMessage> streamAssistantReply(
            String sessionId, String userPrompt) {
        return chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .user(userPrompt)
                .stream(); // Server-Sent Events(WebFlux)로 응답 조각 수신
    }
    ----------------------------------------------------- */
}