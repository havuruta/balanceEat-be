package com.balanceeat.demo.domain.ai.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChatMessage {
    private Long messageId;

    private Long chatSessionId;
    private MessageRole role;
    private String content;
    private LocalDateTime createdAt;
    
    public enum MessageRole {
        USER,
        ASSISTANT
    }
} 