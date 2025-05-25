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
    
    // 간식 영양 정보
    private double snackCalories;
    
    // 야식 영양 정보
    private double nightCalories;
    
    // 일일 총 영양 정보
    private double totalCalories;
    private int totalProtein;
    private int totalFat;
    private int totalCarbohydrates;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static DietSummaryDTO fromEntity(DietSummary entity) {
        if (entity == null) {
            return null;
        }
        
       return DietSummaryDTO.builder().build();
    }
    
    private static int roundToInt(Double value) {
        if (value == null) {
            return 0;
        }
        return (int) Math.round(value);
    }
} 