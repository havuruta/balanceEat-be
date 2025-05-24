package com.balanceeat.demo.domain.nutrition.service.impl;

import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.entity.FoodCategory;
import com.balanceeat.demo.domain.nutrition.dto.NutritionResponseDTO;
import com.balanceeat.demo.domain.nutrition.dto.PageResponseDTO;
import com.balanceeat.demo.domain.nutrition.mapper.NutritionMapper;
import com.balanceeat.demo.domain.nutrition.service.NutritionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NutritionServiceImpl implements NutritionService {

    private final NutritionMapper nutritionMapper;

    @Override
    public Nutrition getNutritionById(Long id) {
        log.info("Fetching nutrition with id: {}", id);
        Nutrition nutrition = nutritionMapper.selectById(id);
        if (nutrition == null) {
            throw new IllegalArgumentException("영양 정보를 찾을 수 없습니다.");
        }
        return nutrition;
    }

    @Override
    public PageResponseDTO<NutritionResponseDTO> getAllNutritions(int page, int size) {
        log.info("Fetching all nutritions with page: {}, size: {}", page, size);
        
        int offset = page * size;
        List<Nutrition> nutritions = nutritionMapper.selectAll(offset, size);
        long total = nutritionMapper.countAll();
        
        log.info("Found {} nutritions", nutritions.size());
        List<NutritionResponseDTO> content = nutritions.stream()
                .map(NutritionResponseDTO::from)
                .collect(Collectors.toList());
        
        return PageResponseDTO.of(content, page, size, total);
    }

    @Override
    public PageResponseDTO<NutritionResponseDTO> searchNutritions(String name, String category, int page, int size) {
        log.info("Searching nutritions - name: {}, category: {}, page: {}, size: {}", name, category, page, size);
        
        int offset = page * size;
        List<Nutrition> nutritions;
        long total;

        if (name != null && !name.isEmpty()) {
            nutritions = nutritionMapper.searchByName(name, offset, size);
            total = nutritionMapper.countByName(name);
        } else if (category != null && !category.isEmpty()) {
            FoodCategory foodCategory = FoodCategory.valueOf(category);
            nutritions = nutritionMapper.searchByCategory(foodCategory, offset, size);
            total = nutritionMapper.countByCategory(foodCategory);
        } else {
            nutritions = nutritionMapper.selectAll(offset, size);
            total = nutritionMapper.countAll();
        }

        List<NutritionResponseDTO> content = nutritions.stream()
                .map(NutritionResponseDTO::from)
                .collect(Collectors.toList());
        
        log.info("Found {} nutritions", nutritions.size());
        return PageResponseDTO.of(content, page, size, total);
    }
} 