package com.balanceeat.demo.domain.chat.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChatSession {
    private String sessionId;
    private String title;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 