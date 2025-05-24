package com.balanceeat.demo.domain.nutrition.service;

import com.balanceeat.demo.domain.nutrition.dto.NutritionResponseDTO;
import com.balanceeat.demo.domain.nutrition.dto.PageResponseDTO;
import com.balanceeat.demo.domain.nutrition.entity.Nutrition;

public interface NutritionService {
    Nutrition getNutritionById(Long id);
    PageResponseDTO<NutritionResponseDTO> getAllNutritions(int page, int size);
    PageResponseDTO<NutritionResponseDTO> searchNutritions(String name, String category, int page, int size);
} 