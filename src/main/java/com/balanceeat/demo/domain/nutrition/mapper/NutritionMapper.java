package com.balanceeat.demo.domain.nutrition.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.balanceeat.demo.domain.nutrition.entity.Nutrition;

@Mapper
public interface NutritionMapper {
	void insertNutrition(Nutrition nutrition);
	
	List<Nutrition> selectAll();
	
	Nutrition selectById(@Param("id") Long id);
	
	void updateNutrition(Nutrition nutrition);
	
	void deleteNutrition(@Param("id") Long id);
	
	List<Nutrition> searchByDescription(@Param("description") String description);
	
	List<Nutrition> searchByName(@Param("name") String name);
}
