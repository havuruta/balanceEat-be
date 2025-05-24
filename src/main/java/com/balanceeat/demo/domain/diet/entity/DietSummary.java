package com.balanceeat.demo.domain.diet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DietSummary {
    private Long id;
    private Long userId;
    private LocalDate summaryDate;
    
    private int breakfastCalories;
    private int lunchCalories;
    private int dinnerCalories;
    private int snackCalories;
    private int nightCalories;
    
    // 일일 총 영양 정보
    private int totalCalories;
    private int totalProtein;
    private int totalFat;
    private int totalCarbohydrates;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DietSummary create(Long userId, LocalDate summaryDate) {
        DietSummary summary = new DietSummary();
        summary.userId = userId;
        summary.summaryDate = summaryDate;
        summary.breakfastCalories = 0;
        summary.lunchCalories = 0;
        summary.dinnerCalories = 0;
        summary.snackCalories = 0;
        summary.nightCalories = 0;
        summary.totalCalories = 0;
        summary.totalProtein = 0;
        summary.totalFat = 0;
        summary.totalCarbohydrates = 0;
        return summary;
    }

    public void updateCalories(int breakfast, int lunch, int dinner, 
                             int snack, int night, int total) {
        this.breakfastCalories = breakfast;
        this.lunchCalories = lunch;
        this.dinnerCalories = dinner;
        this.snackCalories = snack;
        this.nightCalories = night;
        this.totalCalories = total;
    }

    public void updateNutrition(int protein, int fat, int carbs) {
        this.totalProtein = protein;
        this.totalFat = fat;
        this.totalCarbohydrates = carbs;
    }
} 