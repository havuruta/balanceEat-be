package com.balanceeat.demo.domain.diet.service;

import java.time.LocalDate;
import java.util.List;

import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietUpdateRequestDTO;
import com.balanceeat.demo.domain.diet.mapper.DietMapper;
import com.balanceeat.demo.domain.diet.mapper.DietSummaryMapper;
import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.mapper.NutritionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

public interface DietService {
    void batchAddDiets(DietAddRequestDTO request, Long userId);
    List<DietSummary> getDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end);
    void addDiet(Diet diet);
    void updateDiet(Long dietId, DietUpdateRequestDTO request, Long userId);
    void deleteDiet(Long id);
    List<Diet> getDietsByDate(Long userId, LocalDate date);
} 