package com.balanceeat.demo.domain.diet.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Long id;
    private Long userId;
    private LocalDate summaryDate;
    
    private int breakfastCalories;
    private int lunchCalories;
    private int dinnerCalories;
    private double snackCalories;
    private double nightCalories;
    private double totalCalories;
    private double totalProtein;
    private double totalFat;
    private double totalCarbohydrates;
    
    public static DietSummaryDTO fromEntity(DietSummary entity) {
        if (entity == null) {
            return null;
        }
        
        return DietSummaryDTO.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .summaryDate(entity.getSummaryDate())
            .breakfastCalories((int) entity.getBreakfastCalories())
            .lunchCalories((int) entity.getLunchCalories())
            .dinnerCalories((int) entity.getDinnerCalories())
            .snackCalories(entity.getSnackCalories())
            .nightCalories(entity.getNightCalories())
            .totalCalories(entity.getTotalCalories())
            .build();
    }
    
    private static int roundToInt(Double value) {
        if (value == null) {
            return 0;
        }
        return (int) Math.round(value);
    }
} 