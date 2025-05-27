package com.balanceeat.demo.ai.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatResponseDTO {
    private String content;
    private Integer score;
    private String feedback;
    private String calorieAnalysis;
    private String nutrientAnalysis;
    private String suggestions1;
    private String suggestions2;
    private String suggestions3;
} 