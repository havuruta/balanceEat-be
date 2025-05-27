package com.balanceeat.demo.ai.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UserDietDTO {
    private UserInfoDTO userInfo;
    private double totalCalories;
    private double totalCarbohydrates;
    private double totalProtein;
    private double totalFat;
    private List<MealDTO> meals;
    private DietSummaryDTO dietSummary;

    @Getter
    @Setter
    public static class MealDTO {
        private String mealType;
        private List<FoodDTO> foods;
    }

    @Getter
    @Setter
    public static class FoodDTO {
        private String name;
        private double amount;
        private double calories;
    }

    @Getter
    @Setter
    public static class DietSummaryDTO {
        private double breakfastCalories;
        private double lunchCalories;
        private double dinnerCalories;
        private double snackCalories;
        private double nightCalories;
        private double totalCalories;
        private double totalCarbohydrates;
        private double totalProtein;
        private double totalFat;
    }
} 