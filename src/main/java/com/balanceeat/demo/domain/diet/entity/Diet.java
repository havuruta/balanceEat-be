package com.balanceeat.demo.domain.diet.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Diet {
    private Long id;
    private Long userId;
    private LocalDate dietDate;
    private String mealType; // BREAKFAST, LUNCH, DINNER
    private String foodName;
    private Double amount; // g 단위
    private Double calories;
    private Double protein;
    private Double fat;
    private Double carbohydrates;
    private String note; // 추가 메모
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 