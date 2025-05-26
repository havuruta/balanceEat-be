package com.balanceeat.demo.domain.diet.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class DietTest {

    @Test
    @DisplayName("Diet 엔티티 생성 테스트")
    void createDiet() {
        // given
        Long userId = 1L;
        Long nutritionId = 1L;
        String foodName = "테스트 음식";
        Integer amount = 100;
        String note = "테스트 메모";
        MealType mealType = MealType.BREAKFAST;
        LocalDate dietDate = LocalDate.now();
        LocalTime mealTime = LocalTime.now();

        // when
        Diet diet = Diet.create(
            userId,
            nutritionId,
            foodName,
            amount,
            note,
            mealType,
            dietDate,
            mealTime
        );

        // then
        assertThat(diet.getUserId()).isEqualTo(userId);
        assertThat(diet.getNutritionId()).isEqualTo(nutritionId);
        assertThat(diet.getFoodName()).isEqualTo(foodName);
        assertThat(diet.getAmount()).isEqualTo(amount);
        assertThat(diet.getNote()).isEqualTo(note);
        assertThat(diet.getMealType()).isEqualTo(mealType);
        assertThat(diet.getDietDate()).isEqualTo(dietDate);
        assertThat(diet.getMealTime()).isEqualTo(mealTime);
    }

    @Test
    @DisplayName("Diet 엔티티 업데이트 테스트")
    void updateDiet() {
        // given
        Diet diet = Diet.create(
            1L,
            1L,
            "테스트 음식",
            100,
            "테스트 메모",
            MealType.BREAKFAST,
            LocalDate.now(),
            LocalTime.now()
        );

        // when
        diet.update(200, "수정된 메모", MealType.LUNCH, LocalTime.of(12, 0));

        // then
        assertThat(diet.getAmount()).isEqualTo(200);
        assertThat(diet.getNote()).isEqualTo("수정된 메모");
        assertThat(diet.getMealType()).isEqualTo(MealType.LUNCH);
        assertThat(diet.getMealTime()).isEqualTo(LocalTime.of(12, 0));
    }

    @Test
    @DisplayName("Diet 엔티티 소유자 확인 테스트")
    void isOwner() {
        // given
        Diet diet = Diet.create(
            1L,
            1L,
            "테스트 음식",
            100,
            "테스트 메모",
            MealType.BREAKFAST,
            LocalDate.now(),
            LocalTime.now()
        );

        // when & then
        assertThat(diet.isOwner(1L)).isTrue();
        assertThat(diet.isOwner(2L)).isFalse();
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