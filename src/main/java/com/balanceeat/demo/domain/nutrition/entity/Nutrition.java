package com.balanceeat.demo.domain.nutrition.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Nutrition {
	private Long id;
	private String name;
	private Double calories;
	private Double protein;
	private Double carbohydrates;
	private Double fat;
	private String description;
}