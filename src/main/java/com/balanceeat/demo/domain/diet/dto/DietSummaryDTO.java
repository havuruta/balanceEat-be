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
    
    private double breakfastCalories;
    private double lunchCalories;
    private double dinnerCalories;
    private double snackCalories;
    private double nightCalories;
    private double totalCalories;
    private double totalProtein;
    private double totalFat;
    private double totalCarbohydrates;
} 