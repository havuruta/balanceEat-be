package com.balanceeat.demo.domain.diet.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.entity.Diet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietSummaryDTO {
    private LocalDate summaryDate;
    
    // 아침 식사 정보
    private List<Diet> breakfast;
    private int breakfastCalories;
    private int breakfastProtein;
    private int breakfastFat;
    private int breakfastCarbohydrates;
    
    // 점심 식사 정보
    private List<Diet> lunch;
    private int lunchCalories;
    private int lunchProtein;
    private int lunchFat;
    private int lunchCarbohydrates;
    
    // 저녁 식사 정보
    private List<Diet> dinner;
    private int dinnerCalories;
    private int dinnerProtein;
    private int dinnerFat;
    private int dinnerCarbohydrates;
    
    // 일일 총 영양 정보
    private int totalCalories;
    private int totalProtein;
    private int totalFat;
    private int totalCarbohydrates;
    
    public static DietSummaryDTO fromEntity(DietSummary entity) {
        if (entity == null) {
            return null;
        }
        
        return DietSummaryDTO.builder()
                .summaryDate(entity.getSummaryDate())
                // 아침 식사
                .breakfast(entity.getBreakfast())
                .breakfastCalories(roundToInt(entity.getBreakfastCalories()))
                .breakfastProtein(roundToInt(entity.getBreakfastProtein()))
                .breakfastFat(roundToInt(entity.getBreakfastFat()))
                .breakfastCarbohydrates(roundToInt(entity.getBreakfastCarbohydrates()))
                // 점심 식사
                .lunch(entity.getLunch())
                .lunchCalories(roundToInt(entity.getLunchCalories()))
                .lunchProtein(roundToInt(entity.getLunchProtein()))
                .lunchFat(roundToInt(entity.getLunchFat()))
                .lunchCarbohydrates(roundToInt(entity.getLunchCarbohydrates()))
                // 저녁 식사
                .dinner(entity.getDinner())
                .dinnerCalories(roundToInt(entity.getDinnerCalories()))
                .dinnerProtein(roundToInt(entity.getDinnerProtein()))
                .dinnerFat(roundToInt(entity.getDinnerFat()))
                .dinnerCarbohydrates(roundToInt(entity.getDinnerCarbohydrates()))
                // 일일 총합
                .totalCalories(roundToInt(entity.getTotalCalories()))
                .totalProtein(roundToInt(entity.getTotalProtein()))
                .totalFat(roundToInt(entity.getTotalFat()))
                .totalCarbohydrates(roundToInt(entity.getTotalCarbohydrates()))
                .build();
    }
    
    private static int roundToInt(Double value) {
        if (value == null) {
            return 0;
        }
        return (int) Math.round(value);
    }
} 