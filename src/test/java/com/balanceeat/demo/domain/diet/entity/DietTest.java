package com.balanceeat.demo.domain.diet.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DietTest {

    @Test
    @DisplayName("Diet 엔티티 생성 테스트")
    void createDiet() {
        // given
        Long userId = 1L;
        LocalDate dietDate = LocalDate.now();
        String mealType = "BREAKFAST";
        String foodName = "테스트 음식";
        int amount = 100;
        double calories = 300.0;
        double protein = 20.0;
        double fat = 10.0;
        double carbohydrates = 40.0;

        // when
        Diet diet = Diet.builder()
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

        // then
        assertThat(diet.getUserId()).isEqualTo(userId);
        assertThat(diet.getDietDate()).isEqualTo(dietDate);
        assertThat(diet.getMealType()).isEqualTo(mealType);
        assertThat(diet.getFoodName()).isEqualTo(foodName);
        assertThat(diet.getAmount()).isEqualTo(amount);
        assertThat(diet.getCalories()).isEqualTo(calories);
        assertThat(diet.getProtein()).isEqualTo(protein);
        assertThat(diet.getFat()).isEqualTo(fat);
        assertThat(diet.getCarbohydrates()).isEqualTo(carbohydrates);
    }

    @Test
    @DisplayName("Diet 엔티티 영양소 계산 테스트")
    void calculateNutrition() {
        // given
        Diet diet = Diet.builder()
                .amount(200)
                .calories(300.0)
                .protein(20.0)
                .fat(10.0)
                .carbohydrates(40.0)
                .build();

        // when
        double calculatedCalories = diet.getCalories() * (diet.getAmount() / 100.0);
        double calculatedProtein = diet.getProtein() * (diet.getAmount() / 100.0);
        double calculatedFat = diet.getFat() * (diet.getAmount() / 100.0);
        double calculatedCarbohydrates = diet.getCarbohydrates() * (diet.getAmount() / 100.0);

        // then
        assertThat(calculatedCalories).isEqualTo(600.0);
        assertThat(calculatedProtein).isEqualTo(40.0);
        assertThat(calculatedFat).isEqualTo(20.0);
        assertThat(calculatedCarbohydrates).isEqualTo(80.0);
    }
} 