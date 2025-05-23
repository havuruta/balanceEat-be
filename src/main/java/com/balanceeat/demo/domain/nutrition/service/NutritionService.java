package com.balanceeat.demo.domain.nutrition.service;

import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import java.util.List;

public interface NutritionService {
    Nutrition getNutritionById(Long id);
    List<Nutrition> getAllNutritions();
    List<Nutrition> searchByDescription(String description);
    List<Nutrition> searchByName(String name);
    // NutritionDTO createNutrition(NutritionDTO nutritionDTO);
    // NutritionDTO updateNutrition(Long id, NutritionDTO nutritionDTO);
    // void deleteNutrition(Long id);
} 