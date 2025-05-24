package com.balanceeat.demo.domain.diet.dto;

import com.balanceeat.demo.domain.diet.entity.MealType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class DietAddRequestDTO {
	
	private List<DietSimpleDTO> diets;
	
	@Getter
	@Setter
	public static class DietSimpleDTO {
		private Long nutritionId;
		private String amount;
		private String note;
		private MealType mealType;
		private String dietDate;
		private LocalTime mealTime;
	}
}
