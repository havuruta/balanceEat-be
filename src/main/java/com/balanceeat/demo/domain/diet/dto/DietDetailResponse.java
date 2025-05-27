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
	private List<DietDTO> night;
	private List<DietDTO> snack;
	private double totalProtein;
	private double totalFat;
	private double totalCarbohydrates;
}