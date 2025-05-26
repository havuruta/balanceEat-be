package com.balanceeat.demo.domain.diet.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;

import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietUpdateRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.dto.DietDetailResponse;
import com.balanceeat.demo.domain.diet.dto.DietDTO;
import com.balanceeat.demo.domain.diet.entity.MealType;

public interface DietService {
    void batchAddDiets(DietAddRequestDTO request, Long userId);
    List<DietSummaryDTO> getDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end);
    void addDiet(Diet diet);
    void updateDiet(Long dietId, DietUpdateRequestDTO request, Long userId);
    void deleteDiet(Long id);
    List<Diet> getDietsByDate(Long userId, LocalDate date);
    DietDetailResponse getDietDetailByDate(LocalDate date, Long userId);
} 