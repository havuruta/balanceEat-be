package com.balanceeat.demo.domain.diet.dto;

import com.balanceeat.demo.domain.diet.entity.MealType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DietUpdateRequestDTO {
    private int amount;
}