package com.balanceeat.demo.domain.diet.service;

import java.time.LocalDate;
import java.util.List;

import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietUpdateRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.entity.Diet;

public interface DietService {
    void batchAddDiets(DietAddRequestDTO request, Long userId);
    List<DietSummaryDTO> getDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end);
    void addDiet(Diet diet);
    void updateDiet(Long dietId, DietUpdateRequestDTO request, Long userId);
    void deleteDiet(Long id);
    List<Diet> getDietsByDate(Long userId, LocalDate date);
} 