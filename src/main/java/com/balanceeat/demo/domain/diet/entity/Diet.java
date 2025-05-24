package com.balanceeat.demo.domain.diet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class Diet {
    private Long id;
    private Long userId;
    private Long nutritionId;
    private Integer amount;
    private String note;
    private MealType mealType;
    private LocalDate dietDate;
    private LocalTime mealTime;

    public static Diet create(Long userId, Long nutritionId, Integer amount, String note, 
                            MealType mealType, LocalDate dietDate, LocalTime mealTime) {
        Diet diet = new Diet();
        diet.userId = userId;
        diet.nutritionId = nutritionId;
        diet.amount = amount;
        diet.note = note;
        diet.mealType = mealType;
        diet.dietDate = dietDate;
        diet.mealTime = mealTime;
        return diet;
    }

    public void update(Integer amount, String note, MealType mealType, LocalTime mealTime) {
        this.amount = amount;
        this.note = note;
        this.mealType = mealType;
        this.mealTime = mealTime;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }
}