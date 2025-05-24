package com.balanceeat.demo.domain.diet.service.impl;

import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietUpdateRequestDTO;
import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.entity.MealType;
import com.balanceeat.demo.domain.diet.mapper.DietMapper;
import com.balanceeat.demo.domain.diet.mapper.DietSummaryMapper;
import com.balanceeat.demo.domain.diet.service.DietService;
import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.mapper.NutritionMapper;
import com.balanceeat.demo.global.exception.DietNotFoundException;
import com.balanceeat.demo.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DietServiceImpl implements DietService {

    private final DietMapper dietMapper;
    private final DietSummaryMapper dietSummaryMapper;
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
            .map(dto -> Diet.create(
                userId,
                dto.getNutritionId(),
                Integer.parseInt(dto.getAmount()),
                dto.getNote(),
                dto.getMealType(),
                LocalDate.parse(dto.getDietDate()),
                dto.getMealTime()
            ))
            .collect(Collectors.toList());
        
        dietMapper.batchInsert(diets);
        
        // 3. DietSummary 업데이트
        LocalDate summaryDate = LocalDate.parse(request.getDiets().get(0).getDietDate());
        DietSummary summary = dietSummaryMapper.findByDateAndUserId(summaryDate, userId);
        
        if (summary == null) {
            summary = DietSummary.create(userId, summaryDate);
        }
        
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
        
        int totalProtein = diets.stream()
            .mapToInt(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return (int)(nutrition.getProtein() * diet.getAmount() / 100);
            })
            .sum();
        
        int totalFat = diets.stream()
            .mapToInt(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return (int)(nutrition.getFat() * diet.getAmount() / 100);
            })
            .sum();
        
        int totalCarbs = diets.stream()
            .mapToInt(diet -> {
                Nutrition nutrition = nutritionMap.get(diet.getNutritionId());
                return (int)(nutrition.getCarbohydrates() * diet.getAmount() / 100);
            })
            .sum();
        
        summary.updateCalories(
            mealTypeCalories.getOrDefault(MealType.BREAKFAST, 0),
            mealTypeCalories.getOrDefault(MealType.LUNCH, 0),
            mealTypeCalories.getOrDefault(MealType.DINNER, 0),
            mealTypeCalories.getOrDefault(MealType.SNACK, 0),
            mealTypeCalories.getOrDefault(MealType.NIGHT, 0),
            totalCalories
        );
        
        summary.updateNutrition(totalProtein, totalFat, totalCarbs);
        
        if (summary.getId() == null) {
            dietSummaryMapper.insert(summary);
        } else {
            dietSummaryMapper.update(summary);
        }
    }

    @Override
    public List<DietSummary> getDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end) {
        log.debug("식단 요약 조회: 사용자 ID={}, 기간={} ~ {}", userId, start, end);
        return dietSummaryMapper.findByDateRange(userId, start, end);
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
        
        diet.update(
            request.getAmount(),
            request.getNote(),
            request.getMealType(),
            LocalTime.parse(request.getMealTime())
        );
        
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
} 