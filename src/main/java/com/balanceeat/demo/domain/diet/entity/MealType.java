package com.balanceeat.demo.domain.diet.entity;

public enum MealType {
    BREAKFAST("아침"),
    LUNCH("점심"),
    DINNER("저녁"),
    SNACK("간식"),
    NIGHT("야식");

    private final String description;

    MealType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 