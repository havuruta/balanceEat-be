package com.balanceeat.demo.domain.diet.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diet {
    private Long id;
    private Long userId;
    private Long nutritionId;
    private String foodName;
    private Integer amount;
    private String note;
    private MealType mealType;
    private LocalDate dietDate;
    private LocalTime mealTime;

    public static Diet create(Long userId, Long nutritionId, String foodName, Integer amount, String note, 
                            MealType mealType, LocalDate dietDate, LocalTime mealTime) {
        Diet diet = new Diet();
        diet.userId = userId;
        diet.nutritionId = nutritionId;
        diet.foodName = foodName;
        diet.amount = amount;
        diet.note = note;
        diet.mealType = mealType;
        diet.dietDate = dietDate;
        diet.mealTime = mealTime;
        return diet;
    }

    public void updateAmount(Integer amount) {
        this.amount = amount;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }
}