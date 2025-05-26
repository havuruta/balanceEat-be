package com.balanceeat.demo.domain.ai.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 분석 결과를 저장하는 엔티티
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietScoreResult {
    private Long userId;
    private LocalDate date;
    private Integer score;
    private String feedback;
    private LocalDateTime createdAt;
}
