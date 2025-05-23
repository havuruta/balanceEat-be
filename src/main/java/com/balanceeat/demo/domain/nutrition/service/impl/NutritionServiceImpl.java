package com.balanceeat.demo.domain.nutrition.service.impl;

import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.mapper.NutritionMapper;
import com.balanceeat.demo.domain.nutrition.service.NutritionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NutritionServiceImpl implements NutritionService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionServiceImpl.class);
    private final NutritionMapper nutritionMapper;

    @Autowired
    public NutritionServiceImpl(NutritionMapper nutritionMapper) {
        this.nutritionMapper = nutritionMapper;
    }

    @Override
    public Nutrition getNutritionById(Long id) {
        logger.info("ID로 영양 정보 조회: {}", id);
        Nutrition nutrition = nutritionMapper.selectById(id);
        logger.info("조회 결과: {}", nutrition);
        return nutrition;
    }

    @Override
    public List<Nutrition> getAllNutritions() {
        logger.info("전체 영양 정보 조회");
        List<Nutrition> nutritions = nutritionMapper.selectAll();
        logger.info("조회된 전체 항목 수: {}", nutritions.size());
        return nutritions;
    }

    @Override
    public List<Nutrition> searchByDescription(String description) {
        logger.info("설명으로 영양 정보 검색: {}", description);
        List<Nutrition> nutritions = nutritionMapper.searchByDescription(description);
        logger.info("검색 결과 수: {}", nutritions.size());
        return nutritions;
    }
    
    @Override
    public List<Nutrition> searchByName(String name) {
        List<Nutrition> nutritions = nutritionMapper.searchByName(name);
        return nutritions;
    }
    
    // @Override
    // public NutritionDTO createNutrition(NutritionDTO nutritionDTO) {
    //     Nutrition nutrition = convertToEntity(nutritionDTO);
    //     nutritionMapper.insertNutrition(nutrition);
    //     return convertToDTO(nutrition);
    // }
    //
    // @Override
    // public NutritionDTO updateNutrition(Long id, NutritionDTO nutritionDTO) {
    //     Nutrition nutrition = convertToEntity(nutritionDTO);
    //     nutrition.setId(id);
    //     nutritionMapper.updateNutrition(nutrition);
    //     return convertToDTO(nutrition);
    // }
    //
    // @Override
    // public void deleteNutrition(Long id) {
    //     nutritionMapper.deleteNutrition(id);
    // }
    //
    // private NutritionDTO convertToDTO(Nutrition nutrition) {
    //     if (nutrition == null) return null;
    //
    //     return NutritionDTO.builder()
    //             .id(nutrition.getId())
    //             .name(nutrition.getName())
    //             .calories(nutrition.getCalories())
    //             .protein(nutrition.getProtein())
    //             .carbohydrates(nutrition.getCarbohydrates())
    //             .fat(nutrition.getFat())
    //             .description(nutrition.getDescription())
    //             .build();
    // }
    //
    // private Nutrition convertToEntity(NutritionDTO dto) {
    //     if (dto == null) return null;
    //
    //     return Nutrition.builder()
    //             .id(dto.getId())
    //             .name(dto.getName())
    //             .calories(dto.getCalories())
    //             .protein(dto.getProtein())
    //             .carbohydrates(dto.getCarbohydrates())
    //             .fat(dto.getFat())
    //             .description(dto.getDescription())
    //             .build();
    // }
} 