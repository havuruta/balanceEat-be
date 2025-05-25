package com.balanceeat.demo.domain.diet.service;

import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.entity.MealType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DietSummaryService {
    void updateSummary(Long userId, LocalDate date, Map<MealType, Integer> mealTypeCalories, int totalCalories);
    List<DietSummaryDTO> getSummariesByDateRange(Long userId, LocalDate start, LocalDate end);
    DietSummary getSummaryByDate(Long userId, LocalDate date);
    DietSummaryDTO createSummary(DietSummaryDTO dto);
    DietSummaryDTO updateSummary(DietSummaryDTO dto);
} 