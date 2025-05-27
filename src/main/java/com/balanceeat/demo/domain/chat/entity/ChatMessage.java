package com.balanceeat.demo.domain.chat.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage {
    private Long id;
    private String sessionId;
    private MessageRole role;
    private String content;
    private LocalDateTime createdAt;

    public enum MessageRole {
        USER, ASSISTANT
    }
} 