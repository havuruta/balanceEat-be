package com.balanceeat.demo.domain.diet.service;

import java.time.LocalDate;
import java.util.List;

import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;

public interface DietService {
    List<DietSummary> getDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end);
    void addDiet(Diet diet);
    void updateDiet(Diet diet);
    void deleteDiet(Long id);
    List<Diet> getDietsByDate(Long userId, LocalDate date);
} 