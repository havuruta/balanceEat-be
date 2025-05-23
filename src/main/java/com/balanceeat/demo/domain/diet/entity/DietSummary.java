package com.balanceeat.demo.domain.diet.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class DietSummary {
    private Long id;
    private Long userId;
    private LocalDate summaryDate;
    
    // 아침 식사 정보
    private List<Diet> breakfast;
    private Double breakfastCalories;
    private Double breakfastProtein;
    private Double breakfastFat;
    private Double breakfastCarbohydrates;
    
    // 점심 식사 정보
    private List<Diet> lunch;
    private Double lunchCalories;
    private Double lunchProtein;
    private Double lunchFat;
    private Double lunchCarbohydrates;
    
    // 저녁 식사 정보
    private List<Diet> dinner;
    private Double dinnerCalories;
    private Double dinnerProtein;
    private Double dinnerFat;
    private Double dinnerCarbohydrates;
    
    // 일일 총 영양 정보
    private Double totalCalories;
    private Double totalProtein;
    private Double totalFat;
    private Double totalCarbohydrates;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 