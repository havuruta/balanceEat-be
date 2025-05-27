package com.balanceeat.demo.domain.dietScoreResult.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private String calorieAnalysis;
    private String nutrientAnalysis;
    private String suggestions1;
    private String suggestions2;
    private String suggestions3;
    private LocalDateTime createdAt;
}
