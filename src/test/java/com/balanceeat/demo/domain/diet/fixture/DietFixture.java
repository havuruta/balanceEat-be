package com.balanceeat.demo.domain.diet.fixture;

import com.balanceeat.demo.domain.diet.entity.Diet;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietFixture {
    private Long id;
    private Long userId;
    private LocalDate dietDate;
    private String mealType;
    private String foodName;
    private double amount;
    private double calories;
    private double protein;
    private double fat;
    private double carbohydrates;

    public Diet instance() {
        return Diet.builder()
                .id(id != null ? id : 1L)
                .userId(userId)
                .dietDate(dietDate)
                .mealType(mealType)
                .foodName(foodName)
                .amount(amount)
                .calories(calories)
                .protein(protein)
                .fat(fat)
                .carbohydrates(carbohydrates)
                .build();
    }

    public static DietFixture breakfast(Long userId, LocalDate date, double calories, double protein, double fat, double carbohydrates) {
        return DietFixture.builder()
                .id(1L)
                .userId(userId)
                .dietDate(date)
                .mealType("BREAKFAST")
                .foodName("아침 식사")
                .amount(100.0)
                .calories(calories)
                .protein(protein)
                .fat(fat)
                .carbohydrates(carbohydrates)
                .build();
    }

    public static DietFixture lunch(Long userId, LocalDate date, double calories, double protein, double fat, double carbohydrates) {
        return DietFixture.builder()
                .id(2L)
                .userId(userId)
                .dietDate(date)
                .mealType("LUNCH")
                .foodName("점심 식사")
                .amount(100.0)
                .calories(calories)
                .protein(protein)
                .fat(fat)
                .carbohydrates(carbohydrates)
                .build();
    }

    public static DietFixture dinner(Long userId, LocalDate date, double calories, double protein, double fat, double carbohydrates) {
        return DietFixture.builder()
                .id(3L)
                .userId(userId)
                .dietDate(date)
                .mealType("DINNER")
                .foodName("저녁 식사")
                .amount(100.0)
                .calories(calories)
                .protein(protein)
                .fat(fat)
                .carbohydrates(carbohydrates)
                .build();
    }
} 