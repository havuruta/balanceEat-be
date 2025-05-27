package com.balanceeat.demo.domain.diet.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

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
        
        // 기존 DietSummary 조회
        DietSummary existingSummary = dietSummaryService.getSummaryByDate(userId, summaryDate);
        
        // 새로운 식사 타입별 칼로리 계산
        Map<MealType, Integer> newMealTypeCalories = diets.stream()
            .collect(Collectors.groupingBy(
                Diet::getMealType,
                Collectors.summingInt(diet -> {
                    Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                    return (int)(nutrition.getCalories() * diet.getAmount() / 100);
                })
            ));
        
        // 기존 값과 새로운 값을 합산
        Map<MealType, Integer> mealTypeCalories = new HashMap<>();
        for (MealType type : MealType.values()) {
            double existingCalories = 0;
            if (existingSummary != null) {
                switch (type) {
                    case BREAKFAST: existingCalories = existingSummary.getBreakfastCalories(); break;
                    case LUNCH: existingCalories = existingSummary.getLunchCalories(); break;
                    case DINNER: existingCalories = existingSummary.getDinnerCalories(); break;
                    case SNACK: existingCalories = existingSummary.getSnackCalories(); break;
                    case NIGHT: existingCalories = existingSummary.getNightCalories(); break;
                }
            }
            mealTypeCalories.put(type, (int)existingCalories + newMealTypeCalories.getOrDefault(type, 0));
        }
        
        // 새로운 영양소 계산
        int newTotalCalories = diets.stream()
            .mapToInt(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return (int)(nutrition.getCalories() * diet.getAmount() / 100);
            })
            .sum();
        
        double newTotalProtein = diets.stream()
            .mapToDouble(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return nutrition.getProtein() * diet.getAmount() / 100;
            })
            .sum();
        
        double newTotalFat = diets.stream()
            .mapToDouble(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return nutrition.getFat() * diet.getAmount() / 100;
            })
            .sum();
        
        double newTotalCarbohydrates = diets.stream()
            .mapToDouble(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return nutrition.getCarbohydrates() * diet.getAmount() / 100;
            })
            .sum();
        
        // 기존 값과 새로운 값을 합산
        int totalCalories = newTotalCalories;
        double totalProtein = newTotalProtein;
        double totalFat = newTotalFat;
        double totalCarbohydrates = newTotalCarbohydrates;
        
        if (existingSummary != null) {
            totalCalories += existingSummary.getTotalCalories();
            totalProtein += existingSummary.getTotalProtein();
            totalFat += existingSummary.getTotalFat();
            totalCarbohydrates += existingSummary.getTotalCarbohydrates();
        }
        
        dietSummaryService.updateSummary(userId, summaryDate, mealTypeCalories, totalCalories, 
            totalProtein, totalFat, totalCarbohydrates);
    }

    @Override
    public List<DietSummaryDTO> getDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end) {
        return dietSummaryService.getSummariesByDateRange(userId, start, end);
    }
    
    @Override
    public DietDetailResponse getDietDetailByDate(Long userId, LocalDate date) {
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
    
    @Override
    public void updateDiet(Long dietId, DietUpdateRequestDTO request, Long userId) {
        log.info("식단 수정 서비스 호출: dietId={}, userId={}", dietId, userId);
        
        Diet diet = dietMapper.findById(dietId)
            .orElseThrow(() -> new DietNotFoundException("식단을 찾을 수 없습니다: " + dietId));
        
        if (!diet.isOwner(userId)) {
            throw new UnauthorizedException("식단을 수정할 권한이 없습니다.");
        }

        // 기존 영양소 정보 조회
        List<Nutrition> nutritions = nutritionMapper.findByIds(Collections.singletonList(diet.getNutritionId()));
        if (nutritions.isEmpty()) {
            throw new DietNotFoundException("영양소 정보를 찾을 수 없습니다: " + diet.getNutritionId());
        }
        Nutrition nutrition = nutritions.get(0);

        // 기존 DietSummary 조회
        DietSummary summary = dietSummaryService.getSummaryByDate(userId, diet.getDietDate());
        if (summary != null) {
            // 기존 amount의 영양소 계산
            double oldCalories = nutrition.getCalories() * diet.getAmount() / 100;
            double oldProtein = nutrition.getProtein() * diet.getAmount() / 100;
            double oldFat = nutrition.getFat() * diet.getAmount() / 100;
            double oldCarbs = nutrition.getCarbohydrates() * diet.getAmount() / 100;

            // 새로운 amount의 영양소 계산
            double newCalories = nutrition.getCalories() * request.getAmount() / 100;
            double newProtein = nutrition.getProtein() * request.getAmount() / 100;
            double newFat = nutrition.getFat() * request.getAmount() / 100;
            double newCarbs = nutrition.getCarbohydrates() * request.getAmount() / 100;

            // 식사 타입별 칼로리 계산
            Map<MealType, Integer> mealTypeCalories = new HashMap<>();
            for (MealType type : MealType.values()) {
                double existingCalories = 0;
                switch (type) {
                    case BREAKFAST: existingCalories = summary.getBreakfastCalories(); break;
                    case LUNCH: existingCalories = summary.getLunchCalories(); break;
                    case DINNER: existingCalories = summary.getDinnerCalories(); break;
                    case SNACK: existingCalories = summary.getSnackCalories(); break;
                    case NIGHT: existingCalories = summary.getNightCalories(); break;
                }
                
                // 수정하는 식단의 식사 타입인 경우 칼로리를 업데이트
                if (type == diet.getMealType()) {
                    existingCalories = existingCalories - oldCalories + newCalories;
                }
                mealTypeCalories.put(type, (int)existingCalories);
            }
            
            // 총 영양소 업데이트
            double totalCalories = summary.getTotalCalories() - oldCalories + newCalories;
            double totalProtein = summary.getTotalProtein() - oldProtein + newProtein;
            double totalFat = summary.getTotalFat() - oldFat + newFat;
            double totalCarbohydrates = summary.getTotalCarbohydrates() - oldCarbs + newCarbs;
            
            // DietSummary 업데이트
            dietSummaryService.updateSummary(
                userId, 
                diet.getDietDate(), 
                mealTypeCalories, 
                (int)totalCalories, 
                totalProtein, 
                totalFat, 
                totalCarbohydrates
            );
        }
        
        diet.updateAmount(request.getAmount());
        dietMapper.update(diet);
        log.info("식단 수정 완료");
    }

    @Override
    public void deleteDiet(Long dietId, Long userId) {
        Diet diet = dietMapper.findById(dietId)
            .orElseThrow(() -> new DietNotFoundException("식단을 찾을 수 없습니다: " + dietId));
        
        if (!diet.isOwner(userId)) {
            throw new UnauthorizedException("식단을 수정할 권한이 없습니다.");
        }
        
        // 삭제할 식단의 영양소 정보 조회
        List<Nutrition> nutritions = nutritionMapper.findByIds(Collections.singletonList(diet.getNutritionId()));
        if (nutritions.isEmpty()) {
            throw new DietNotFoundException("영양소 정보를 찾을 수 없습니다: " + diet.getNutritionId());
        }
        Nutrition nutrition = nutritions.get(0);
        
        // 해당 날짜의 DietSummary 조회
        DietSummary summary = dietSummaryService.getSummaryByDate(userId, diet.getDietDate());
        
        if (summary != null) {
            // 삭제할 식단의 영양소 계산
            double caloriesToSubtract = nutrition.getCalories() * diet.getAmount() / 100;
            double proteinToSubtract = nutrition.getProtein() * diet.getAmount() / 100;
            double fatToSubtract = nutrition.getFat() * diet.getAmount() / 100;
            double carbsToSubtract = nutrition.getCarbohydrates() * diet.getAmount() / 100;
            
            // 식사 타입별 칼로리 계산
            Map<MealType, Integer> mealTypeCalories = new HashMap<>();
            for (MealType type : MealType.values()) {
                double existingCalories = 0;
                switch (type) {
                    case BREAKFAST: existingCalories = summary.getBreakfastCalories(); break;
                    case LUNCH: existingCalories = summary.getLunchCalories(); break;
                    case DINNER: existingCalories = summary.getDinnerCalories(); break;
                    case SNACK: existingCalories = summary.getSnackCalories(); break;
                    case NIGHT: existingCalories = summary.getNightCalories(); break;
                }
                
                // 삭제하는 식단의 식사 타입인 경우 칼로리를 빼줌
                if (type == diet.getMealType()) {
                    existingCalories -= caloriesToSubtract;
                }
                mealTypeCalories.put(type, (int)existingCalories);
            }
            
            // 총 영양소에서 빼기
            double totalCalories = summary.getTotalCalories() - caloriesToSubtract;
            double totalProtein = summary.getTotalProtein() - proteinToSubtract;
            double totalFat = summary.getTotalFat() - fatToSubtract;
            double totalCarbohydrates = summary.getTotalCarbohydrates() - carbsToSubtract;
            
            // DietSummary 업데이트
            dietSummaryService.updateSummary(
                userId, 
                diet.getDietDate(), 
                mealTypeCalories, 
                (int)totalCalories, 
                totalProtein, 
                totalFat, 
                totalCarbohydrates
            );
        }
        
        dietMapper.delete(dietId);
    }

    @Override
    public List<DietDTO> getDietsByDate(Long userId, LocalDate date) {
        return dietMapper.findByDate(userId, date).stream()
            .map(diet -> DietDTO.builder()
                .id(diet.getId())
                .foodName(diet.getFoodName())
                .amount(diet.getAmount())
                .mealType(diet.getMealType())
                .note(diet.getNote())
                .mealTime(diet.getMealTime())
                .build())
            .collect(Collectors.toList());
    }
} 