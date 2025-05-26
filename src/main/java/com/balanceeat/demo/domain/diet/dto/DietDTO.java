package com.balanceeat.demo.domain.diet.dto;

import com.balanceeat.demo.domain.diet.entity.MealType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DietDTO {
    private Long id;
    private String foodName;
    private double amount;
    private MealType mealType;
} 