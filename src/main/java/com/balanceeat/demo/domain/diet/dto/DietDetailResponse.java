package com.balanceeat.demo.domain.diet.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DietDetailResponse {
    private List<DietDTO> breakfast;
    private List<DietDTO> lunch;
    private List<DietDTO> dinner;
    private double totalProtein;
    private double totalFat;
    private double totalCarbohydrates;
} 