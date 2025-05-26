package com.balanceeat.demo.domain.diet.dto;

import com.balanceeat.demo.domain.diet.entity.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietDTO {
    private Long id;
    private String foodName;
    private Integer amount;
    private MealType mealType;
    private String note;
    private LocalTime mealTime;
} 