package com.balanceeat.demo.domain.diet.service.impl;

import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.entity.MealType;
import com.balanceeat.demo.domain.diet.mapper.DietSummaryConverter;
import com.balanceeat.demo.domain.diet.mapper.DietSummaryMyBatisMapper;
import com.balanceeat.demo.domain.diet.service.DietSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DietSummaryServiceImpl implements DietSummaryService {

    private final DietSummaryMyBatisMapper dietSummaryMyBatisMapper;
    private final DietSummaryConverter dietSummaryConverter;

    @Override
    public void updateSummary(Long userId, LocalDate date, Map<MealType, Integer> mealTypeCalories, int totalCalories) {
        log.debug("식단 요약 업데이트 요청: 사용자 ID={}, 날짜={}", userId, date);
        DietSummary existingSummary = dietSummaryMyBatisMapper.findByDateAndUserId(date, userId);
        
        if (existingSummary == null) {
            log.debug("새로운 식단 요약 생성");
            DietSummaryDTO dto = new DietSummaryDTO();
            dto.setUserId(userId);
            dto.setSummaryDate(date);
            dto.setBreakfastCalories(mealTypeCalories.getOrDefault(MealType.BREAKFAST, 0));
            dto.setLunchCalories(mealTypeCalories.getOrDefault(MealType.LUNCH, 0));
            dto.setDinnerCalories(mealTypeCalories.getOrDefault(MealType.DINNER, 0));
            dto.setSnackCalories(mealTypeCalories.getOrDefault(MealType.SNACK, 0));
            dto.setNightCalories(mealTypeCalories.getOrDefault(MealType.NIGHT, 0));
            dto.setTotalCalories(totalCalories);
            
            DietSummary newSummary = dietSummaryConverter.toEntity(dto);
            dietSummaryMyBatisMapper.insert(newSummary);
        } else {
            log.debug("기존 식단 요약 업데이트");
            // 기존 값 유지하면서 새로운 값만 더하기
            for (MealType mealType : MealType.values()) {
                int newCalories = mealTypeCalories.getOrDefault(mealType, 0);
                if (newCalories > 0) {  // 새로운 값이 있는 경우에만 더하기
                    switch (mealType) {
                        case BREAKFAST:
                            existingSummary = existingSummary.withBreakfastCalories(
                                existingSummary.getBreakfastCalories() + newCalories);
                            break;
                        case LUNCH:
                            existingSummary = existingSummary.withLunchCalories(
                                existingSummary.getLunchCalories() + newCalories);
                            break;
                        case DINNER:
                            existingSummary = existingSummary.withDinnerCalories(
                                existingSummary.getDinnerCalories() + newCalories);
                            break;
                        case SNACK:
                            existingSummary = existingSummary.withSnackCalories(
                                existingSummary.getSnackCalories() + newCalories);
                            break;
                        case NIGHT:
                            existingSummary = existingSummary.withNightCalories(
                                existingSummary.getNightCalories() + newCalories);
                            break;
                    }
                }
            }
            
            // 총 칼로리 업데이트
            existingSummary = existingSummary.withTotalCalories(
                existingSummary.getTotalCalories() + totalCalories);
            
            dietSummaryMyBatisMapper.update(existingSummary);
        }
    }

    @Override
    public List<DietSummaryDTO> getSummariesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        log.debug("식단 요약 조회: 사용자 ID={}, 기간={} ~ {}", userId, startDate, endDate);
        List<DietSummary> summaries = dietSummaryMyBatisMapper.findByDateRange(userId, startDate, endDate);
        return dietSummaryConverter.toDTOList(summaries);
    }

    @Override
    public DietSummary getSummaryByDate(Long userId, LocalDate date) {
        log.debug("특정 날짜 식단 요약 조회: 사용자 ID={}, 날짜={}", userId, date);
        return dietSummaryMyBatisMapper.findByDateAndUserId(date, userId);
    }

    @Override
    @Transactional
    public DietSummaryDTO createSummary(DietSummaryDTO dto) {
        log.debug("식단 요약 생성 요청: 사용자 ID={}, 날짜={}", dto.getUserId(), dto.getSummaryDate());
        LocalDate date = dto.getSummaryDate();
        Long userId = dto.getUserId();

        DietSummary existingSummary = dietSummaryMyBatisMapper.findByDateAndUserId(date, userId);
        if (existingSummary != null) {
            log.warn("이미 존재하는 식단 요약: 사용자 ID={}, 날짜={}", userId, date);
            throw new IllegalStateException("이미 해당 날짜의 식단 요약이 존재합니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        DietSummary newSummary = dietSummaryConverter.toEntity(dto);
        dietSummaryMyBatisMapper.insert(newSummary);
        return dietSummaryConverter.toDTO(newSummary);
    }

    @Override
    @Transactional
    public DietSummaryDTO updateSummary(DietSummaryDTO dto) {
        log.debug("식단 요약 업데이트 요청: 사용자 ID={}, 날짜={}", dto.getUserId(), dto.getSummaryDate());
        LocalDate date = dto.getSummaryDate();
        Long userId = dto.getUserId();

        DietSummary existingSummary = dietSummaryMyBatisMapper.findByDateAndUserId(date, userId);
        LocalDateTime now = LocalDateTime.now();

        if (existingSummary != null) {
            log.debug("기존 식단 요약 업데이트");
            DietSummary updatedSummary = existingSummary
                .withBreakfastCalories(dto.getBreakfastCalories())
                .withLunchCalories(dto.getLunchCalories())
                .withDinnerCalories(dto.getDinnerCalories())
                .withSnackCalories(dto.getSnackCalories())
                .withNightCalories(dto.getNightCalories())
                .withTotalCalories(dto.getTotalCalories());

            dietSummaryMyBatisMapper.update(updatedSummary);
            return dietSummaryConverter.toDTO(updatedSummary);
        } else {
            log.debug("새로운 식단 요약 생성");
            DietSummary newSummary = dietSummaryConverter.toEntity(dto);
            dietSummaryMyBatisMapper.insert(newSummary);
            return dietSummaryConverter.toDTO(newSummary);
        }
    }
} 