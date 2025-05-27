package com.balanceeat.demo.ai.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatSession {
    private Long sessionId;
    private Long userId;
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 