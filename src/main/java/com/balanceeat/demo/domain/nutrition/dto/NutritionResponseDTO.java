package com.balanceeat.demo.domain.nutrition.dto;

import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NutritionResponseDTO {
    private Long id;
    private String name;
    private Double calories;
    private Double protein;
    private Double fat;
    private Double carbohydrates;
    private String category;

    public static NutritionResponseDTO from(Nutrition nutrition) {
        return NutritionResponseDTO.builder()
                .id(nutrition.getId())
                .name(nutrition.getName())
                .calories(nutrition.getCalories())
                .protein(nutrition.getProtein())
                .fat(nutrition.getFat())
                .carbohydrates(nutrition.getCarbohydrates())
                .category(nutrition.getCategory().getLabel())
                .build();
    }
} 