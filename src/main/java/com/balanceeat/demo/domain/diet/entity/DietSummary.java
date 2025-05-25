package com.balanceeat.demo.domain.diet.entity;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class DietSummary {
    private final Long id;
    private final Long userId;
    private final LocalDate summaryDate;
    private final double breakfastCalories;
    private final double lunchCalories;
    private final double dinnerCalories;
    private final double snackCalories;
    private final double nightCalories;
    private final double totalCalories;
    private final double totalProtein;
    private final double totalFat;
    private final double totalCarbohydrates;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public DietSummary(Long id, Long userId, LocalDate summaryDate, 
                      double breakfastCalories, double lunchCalories, double dinnerCalories,
                      double snackCalories, double nightCalories, double totalCalories,
                      double totalProtein, double totalFat, double totalCarbohydrates,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.summaryDate = summaryDate;
        this.breakfastCalories = breakfastCalories;
        this.lunchCalories = lunchCalories;
        this.dinnerCalories = dinnerCalories;
        this.snackCalories = snackCalories;
        this.nightCalories = nightCalories;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarbohydrates = totalCarbohydrates;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public DietSummary withId(Long id) {
        return new DietSummary(id, userId, summaryDate, 
                             breakfastCalories, lunchCalories, dinnerCalories,
                             snackCalories, nightCalories, totalCalories,
                             totalProtein, totalFat, totalCarbohydrates,
                             createdAt, updatedAt);
    }

    public DietSummary withUpdatedAt(LocalDateTime updatedAt) {
        return new DietSummary(id, userId, summaryDate, 
                             breakfastCalories, lunchCalories, dinnerCalories,
                             snackCalories, nightCalories, totalCalories,
                             totalProtein, totalFat, totalCarbohydrates,
                             createdAt, updatedAt);
    }

    public DietSummary withCreatedAt(LocalDateTime createdAt) {
        return new DietSummary(id, userId, summaryDate, 
                             breakfastCalories, lunchCalories, dinnerCalories,
                             snackCalories, nightCalories, totalCalories,
                             totalProtein, totalFat, totalCarbohydrates,
                             createdAt, updatedAt);
    }

    public DietSummary withBreakfastCalories(double breakfastCalories) {
        return new DietSummary(id, userId, summaryDate, 
                             breakfastCalories, lunchCalories, dinnerCalories,
                             snackCalories, nightCalories, totalCalories,
                             totalProtein, totalFat, totalCarbohydrates,
                             createdAt, updatedAt);
    }

    public DietSummary withLunchCalories(double lunchCalories) {
        return new DietSummary(id, userId, summaryDate, 
                             breakfastCalories, lunchCalories, dinnerCalories,
                             snackCalories, nightCalories, totalCalories,
                             totalProtein, totalFat, totalCarbohydrates,
                             createdAt, updatedAt);
    }

    public DietSummary withDinnerCalories(double dinnerCalories) {
        return new DietSummary(id, userId, summaryDate, 
                             breakfastCalories, lunchCalories, dinnerCalories,
                             snackCalories, nightCalories, totalCalories,
                             totalProtein, totalFat, totalCarbohydrates,
                             createdAt, updatedAt);
    }

    public DietSummary withSnackCalories(double snackCalories) {
        return new DietSummary(id, userId, summaryDate, 
                             breakfastCalories, lunchCalories, dinnerCalories,
                             snackCalories, nightCalories, totalCalories,
                             totalProtein, totalFat, totalCarbohydrates,
                             createdAt, updatedAt);
    }

    public DietSummary withNightCalories(double nightCalories) {
        return new DietSummary(id, userId, summaryDate, 
                             breakfastCalories, lunchCalories, dinnerCalories,
                             snackCalories, nightCalories, totalCalories,
                             totalProtein, totalFat, totalCarbohydrates,
                             createdAt, updatedAt);
    }

    public DietSummary withTotalCalories(double totalCalories) {
        return new DietSummary(id, userId, summaryDate, 
                             breakfastCalories, lunchCalories, dinnerCalories,
                             snackCalories, nightCalories, totalCalories,
                             totalProtein, totalFat, totalCarbohydrates,
                             createdAt, updatedAt);
    }
}