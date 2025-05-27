package com.balanceeat.demo.util;

import lombok.Getter;
import lombok.Builder;

public class RDICalculator {
    
    public enum Gender {
        MALE, FEMALE
    }
    
    public enum ActivityLevel {
        SEDENTARY(1.2),           // 거의 운동하지 않음
        LIGHTLY_ACTIVE(1.375),    // 가벼운 운동 (주 1-3회)
        MODERATELY_ACTIVE(1.55),  // 중간 강도 운동 (주 3-5회)
        VERY_ACTIVE(1.725);       // 강도 높은 운동 (주 6-7회)
        
        private final double factor;
        
        ActivityLevel(double factor) {
            this.factor = factor;
        }
    }
    
    @Getter
    @Builder
    public static class RDIResult {
        private final int totalCalories;
        private final int carbohydrates;
        private final int protein;
        private final int fat;
    }
    
    /**
     * 기본 활동 수준으로 RDI를 계산합니다.
     */
    public static RDIResult calculateRDI(int birthYear, int weight, int height, Gender gender) {
        return calculateRDI(birthYear, weight, height, gender, ActivityLevel.MODERATELY_ACTIVE);
    }
    
    /**
     * 활동 수준을 지정하여 RDI를 계산합니다.
     */
    public static RDIResult calculateRDI(int birthYear, int weight, int height, Gender gender, ActivityLevel activityLevel) {
        int age = 2024 - birthYear;
        double bmr = calculateBMR(age, weight, height, gender);
        int tdee = (int) (bmr * activityLevel.factor);
        
        return RDIResult.builder()
            .totalCalories(tdee)
            .carbohydrates((int) (tdee * 0.48 / 4))  // 48% of calories from carbs (4 cal/g)
            .protein((int) (tdee * 0.20 / 4))        // 20% of calories from protein (4 cal/g)
            .fat((int) (tdee * 0.32 / 9))            // 32% of calories from fat (9 cal/g)
            .build();
    }
    
    /**
     * 체중 감량을 위한 RDI를 계산합니다.
     */
    public static RDIResult calculateWeightLossRDI(int birthYear, int weight, int height, Gender gender) {
        RDIResult maintenance = calculateRDI(birthYear, weight, height, gender);
        int deficit = 500; // 500칼로리 감량
        
        return RDIResult.builder()
            .totalCalories(maintenance.getTotalCalories() - deficit)
            .carbohydrates((int) (maintenance.getTotalCalories() * 0.45 / 4))  // 45% of calories from carbs
            .protein((int) (maintenance.getTotalCalories() * 0.30 / 4))        // 30% of calories from protein
            .fat((int) (maintenance.getTotalCalories() * 0.25 / 9))            // 25% of calories from fat
            .build();
    }
    
    /**
     * 체중 증가를 위한 RDI를 계산합니다.
     */
    public static RDIResult calculateWeightGainRDI(int birthYear, int weight, int height, Gender gender) {
        RDIResult maintenance = calculateRDI(birthYear, weight, height, gender);
        int surplus = 500; // 500칼로리 증가
        
        return RDIResult.builder()
            .totalCalories(maintenance.getTotalCalories() + surplus)
            .carbohydrates((int) (maintenance.getTotalCalories() * 0.50 / 4))  // 50% of calories from carbs
            .protein((int) (maintenance.getTotalCalories() * 0.25 / 4))        // 25% of calories from protein
            .fat((int) (maintenance.getTotalCalories() * 0.25 / 9))            // 25% of calories from fat
            .build();
    }
    
    /**
     * 기초 대사율(BMR)을 계산합니다.
     * Mifflin-St Jeor 방정식 사용
     */
    private static double calculateBMR(int age, int weight, int height, Gender gender) {
        double bmr = 10 * weight + 6.25 * height - 5 * age;
        return gender == Gender.MALE ? bmr + 5 : bmr - 161;
    }
} 