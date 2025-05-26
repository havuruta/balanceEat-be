package com.balanceeat.demo.domain.diet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DietSummary {
    private Long id;
    private Long userId;
    private LocalDate summaryDate;
    private double breakfastCalories;
    private double lunchCalories;
    private double dinnerCalories;
    private double snackCalories;
    private double nightCalories;
    private double totalCalories;
    private double totalProtein;
    private double totalFat;
    private double totalCarbohydrates;
    
    
    public static DietSummary create(Long userId, LocalDate summaryDate) {
        DietSummary summary = new DietSummary();
        summary.userId = userId;
        summary.summaryDate = summaryDate;
        return summary;
    }

    public void update(double breakfastCalories, double lunchCalories, double dinnerCalories,
                      double snackCalories, double nightCalories, double totalCalories,
                      double totalProtein, double totalFat, double totalCarbohydrates) {
        this.breakfastCalories = breakfastCalories;
        this.lunchCalories = lunchCalories;
        this.dinnerCalories = dinnerCalories;
        this.snackCalories = snackCalories;
        this.nightCalories = nightCalories;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarbohydrates = totalCarbohydrates;
    }
}