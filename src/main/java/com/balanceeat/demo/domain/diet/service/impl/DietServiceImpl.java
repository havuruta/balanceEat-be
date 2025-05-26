package com.balanceeat.demo.domain.diet.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietDTO;
import com.balanceeat.demo.domain.diet.dto.DietDetailResponse;
import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.dto.DietUpdateRequestDTO;
import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.entity.MealType;
import com.balanceeat.demo.domain.diet.mapper.DietMapper;
import com.balanceeat.demo.domain.diet.service.DietService;
import com.balanceeat.demo.domain.diet.service.DietSummaryService;
import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.mapper.NutritionMapper;
import com.balanceeat.demo.global.exception.DietNotFoundException;
import com.balanceeat.demo.global.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DietServiceImpl implements DietService {

    private final DietMapper dietMapper;
    private final DietSummaryService dietSummaryService;
    private final NutritionMapper nutritionMapper;

    @Override
    public void batchAddDiets(DietAddRequestDTO request, Long userId) {
        // 1. 영양정보 ID로 실제 영양정보 조회
        List<Nutrition> nutritions = nutritionMapper.findByIds(
            request.getDiets().stream()
                .map(DietAddRequestDTO.DietSimpleDTO::getNutritionId)
                .collect(Collectors.toList())
        );
        
        Map<Long, Nutrition> nutritionMap = nutritions.stream()
            .collect(Collectors.toMap(Nutrition::getId, n -> n));
        
        // 2. 식단 저장
        List<Diet> diets = request.getDiets().stream()
            .map(dto -> {
                Nutrition nutrition = nutritionMap.get(dto.getNutritionId());
                return Diet.create(
                    userId,
                    dto.getNutritionId(),
                    dto.getFoodName(),
                    Integer.parseInt(dto.getAmount()),
                    dto.getNote(),
                    dto.getMealType(),
                    LocalDate.parse(dto.getDietDate()),
                    dto.getMealTime()
                );
            })
            .collect(Collectors.toList());
        
        dietMapper.batchInsert(diets);
        
        // 3. DietSummary 업데이트
        LocalDate summaryDate = LocalDate.parse(request.getDiets().get(0).getDietDate());
        
        // 식사 타입별 칼로리 계산
        Map<MealType, Integer> mealTypeCalories = diets.stream()
            .collect(Collectors.groupingBy(
                Diet::getMealType,
                Collectors.summingInt(diet -> {
                    Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                    return (int)(nutrition.getCalories() * diet.getAmount() / 100);
                })
            ));
        
        // 일일 총 영양 정보 계산
        int totalCalories = diets.stream()
            .mapToInt(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return (int)(nutrition.getCalories() * diet.getAmount() / 100);
            })
            .sum();
        
        double totalProtein = diets.stream()
            .mapToDouble(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return nutrition.getProtein() * diet.getAmount() / 100;
            })
            .sum();
        
        double totalFat = diets.stream()
            .mapToDouble(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return nutrition.getFat() * diet.getAmount() / 100;
            })
            .sum();
        
        double totalCarbohydrates = diets.stream()
            .mapToDouble(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return nutrition.getCarbohydrates() * diet.getAmount() / 100;
            })
            .sum();
        
        dietSummaryService.updateSummary(userId, summaryDate, mealTypeCalories, totalCalories, 
            totalProtein, totalFat, totalCarbohydrates);
    }

    @Override
    public List<DietSummaryDTO> getDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end) {
        return dietSummaryService.getSummariesByDateRange(userId, start, end);
    }

    @Override
    public void addDiet(Diet diet) {
        dietMapper.insert(diet);
    }

    @Override
    public void updateDiet(Long dietId, DietUpdateRequestDTO request, Long userId) {
        log.info("식단 수정 서비스 호출: dietId={}, userId={}", dietId, userId);
        
        Diet diet = dietMapper.findById(dietId)
            .orElseThrow(() -> new DietNotFoundException("식단을 찾을 수 없습니다: " + dietId));
        
        if (!diet.isOwner(userId)) {
            throw new UnauthorizedException("식단을 수정할 권한이 없습니다.");
        }

        // null-safe: 값이 없으면 기존 값 유지
        Integer amount = request.getAmount() != null ? request.getAmount() : diet.getAmount();
        String note = request.getNote() != null ? request.getNote() : diet.getNote();
        MealType mealType = request.getMealType() != null ? request.getMealType() : diet.getMealType();

        diet.update(amount);
        
        dietMapper.update(diet);
        log.info("식단 수정 완료");
    }

    @Override
    public void deleteDiet(Long id) {
        dietMapper.delete(id);
    }

    @Override
    public List<Diet> getDietsByDate(Long userId, LocalDate date) {
        return dietMapper.findByDate(userId, date);
    }

    @Override
    public DietDetailResponse getDietDetailByDate(LocalDate date, Long userId) {
        List<Diet> diets = dietMapper.findByDate(userId, date);
        DietSummary summary = dietSummaryService.getSummaryByDate(userId, date);
        
        Map<MealType, List<Diet>> dietsByMealType = diets.stream()
            .collect(Collectors.groupingBy(Diet::getMealType));
        
        return DietDetailResponse.builder()
            .breakfast(dietsByMealType.getOrDefault(MealType.BREAKFAST, Collections.emptyList())
                .stream()
                .map(diet -> DietDTO.builder()
                    .id(diet.getId())
                    .foodName(diet.getFoodName())
                    .amount(diet.getAmount())
                    .mealType(diet.getMealType())
                    .build())
                .collect(Collectors.toList()))
            .lunch(dietsByMealType.getOrDefault(MealType.LUNCH, Collections.emptyList())
                .stream()
                .map(diet -> DietDTO.builder()
                    .id(diet.getId())
                    .foodName(diet.getFoodName())
                    .amount(diet.getAmount())
                    .mealType(diet.getMealType())
                    .build())
                .collect(Collectors.toList()))
            .dinner(dietsByMealType.getOrDefault(MealType.DINNER, Collections.emptyList())
                .stream()
                .map(diet -> DietDTO.builder()
                    .id(diet.getId())
                    .foodName(diet.getFoodName())
                    .amount(diet.getAmount())
                    .mealType(diet.getMealType())
                    .build())
                .collect(Collectors.toList()))
            .snack(dietsByMealType.getOrDefault(MealType.SNACK, Collections.emptyList())
                .stream()
                .map(diet -> DietDTO.builder()
                    .id(diet.getId())
                    .foodName(diet.getFoodName())
                    .amount(diet.getAmount())
                    .mealType(diet.getMealType())
                    .build())
                .collect(Collectors.toList()))
            .night(dietsByMealType.getOrDefault(MealType.NIGHT, Collections.emptyList())
                .stream()
                .map(diet -> DietDTO.builder()
                    .id(diet.getId())
                    .foodName(diet.getFoodName())
                    .amount(diet.getAmount())
                    .mealType(diet.getMealType())
                    .build())
                .collect(Collectors.toList()))
            .totalProtein(summary.getTotalProtein())
            .totalFat(summary.getTotalFat())
            .totalCarbohydrates(summary.getTotalCarbohydrates())
            .build();
    }
} 