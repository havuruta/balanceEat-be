package com.balanceeat.demo.domain.nutrition.service.impl;

import com.balanceeat.demo.domain.nutrition.dto.NutritionResponseDTO;
import com.balanceeat.demo.domain.nutrition.dto.PageResponseDTO;
import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.entity.FoodCategory;
import com.balanceeat.demo.domain.nutrition.mapper.NutritionMapper;
import com.balanceeat.demo.domain.nutrition.service.NutritionService;
import com.balanceeat.demo.domain.nutrition.exception.NutritionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class NutritionServiceImpl implements NutritionService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionServiceImpl.class);
    private final NutritionMapper nutritionMapper;

    @Override
    public Nutrition getNutritionById(Long id) {
        Nutrition nutrition = nutritionMapper.selectById(id);
        if (nutrition == null) {
            throw new NutritionNotFoundException();
        }
        return nutrition;
    }

    @Override
    public PageResponseDTO<NutritionResponseDTO> getAllNutritions(int page, int size) {
        logger.info("전체 영양 정보 조회 - 페이지: {}, 크기: {}", page, size);
        int offset = page * size;
        
        List<Nutrition> nutritions = nutritionMapper.selectAll(offset, size);
        long totalElements = nutritionMapper.countAll();
        
        List<NutritionResponseDTO> content = nutritions.stream()
            .map(NutritionResponseDTO::from)
            .collect(Collectors.toList());
            
        return PageResponseDTO.of(content, page, size, totalElements);
    }

    @Override
    public PageResponseDTO<NutritionResponseDTO> searchNutritions(String name, String category, int page, int size) {
        logger.info("영양 정보 검색 - 이름: {}, 카테고리: {}, 페이지: {}, 크기: {}", name, category, page, size);
        int offset = page * size;
        
        List<Nutrition> nutritions;
        long totalElements;
        
        if (name != null && !name.isEmpty()) {
            nutritions = nutritionMapper.searchByName(name, offset, size);
            totalElements = nutritionMapper.countByName(name);
        } else if (category != null && !category.isEmpty()) {
            try {
                FoodCategory foodCategory = FoodCategory.valueOf(category);
                String label = foodCategory.getLabel();
                nutritions = nutritionMapper.searchByCategory(label, offset, size);
                totalElements = nutritionMapper.countByCategory(label);
            } catch (IllegalArgumentException e) {
                logger.error("유효하지 않은 카테고리: {}", category, e);
                throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + category);
            }
        } else {
            nutritions = nutritionMapper.selectAll(offset, size);
            totalElements = nutritionMapper.countAll();
        }
        
        List<NutritionResponseDTO> content = nutritions.stream()
            .map(NutritionResponseDTO::from)
            .collect(Collectors.toList());
            
        return PageResponseDTO.of(content, page, size, totalElements);
    }
} 