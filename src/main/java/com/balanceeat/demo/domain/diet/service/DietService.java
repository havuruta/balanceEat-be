package com.balanceeat.demo.domain.diet.service;

import java.time.LocalDate;
import java.util.List;

import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietDTO;
import com.balanceeat.demo.domain.diet.dto.DietDetailResponse;
import com.balanceeat.demo.domain.diet.dto.DietUpdateRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;

public interface DietService {
    void batchAddDiets(DietAddRequestDTO request, Long userId);
    List<DietSummaryDTO> getDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end);
    void updateDiet(Long dietId, DietUpdateRequestDTO request, Long userId);
    void deleteDiet(Long id, Long userId);
    List<DietDTO> getDietsByDate(Long userId, LocalDate date);
    DietDetailResponse getDietDetailByDate(Long userId, LocalDate date);
}