package com.balanceeat.demo.domain.diet.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.entity.MealType;

public interface DietSummaryService {
    void updateSummary(Long userId, LocalDate date, Map<MealType, Integer> mealTypeCalories,
        int totalCalories, double totalProtein, double totalFat, double totalCarbohydrates);
    
    List<DietSummaryDTO> getSummariesByDateRange(Long userId, LocalDate start, LocalDate end);
    
    
    DietSummary getSummaryByDate(Long userId, LocalDate date);
    DietSummaryDTO getSummaryDTOByDate(Long userId, LocalDate date);
    
    DietSummaryDTO createSummary(DietSummaryDTO dto);
    DietSummaryDTO updateSummary(DietSummaryDTO dto);
} 