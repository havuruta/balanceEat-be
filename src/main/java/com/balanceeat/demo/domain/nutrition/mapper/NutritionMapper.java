package com.balanceeat.demo.domain.nutrition.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.entity.FoodCategory;

@Mapper
public interface NutritionMapper {
	void insertNutrition(Nutrition nutrition);
	
	List<Nutrition> selectAll(@Param("offset") int offset, @Param("limit") int limit);
	
	long countAll();
	
	Nutrition selectById(Long id);
	
	void updateNutrition(Nutrition nutrition);
	
	void deleteNutrition(@Param("id") Long id);
	
	List<Nutrition> searchByDescription(@Param("description") String description, @Param("offset") int offset, @Param("limit") int limit);
	
	long countByDescription(@Param("description") String description);
	
	List<Nutrition> searchByName(@Param("name") String name, @Param("offset") int offset, @Param("limit") int limit);
	
	long countByName(@Param("name") String name);
	
	List<Nutrition> findByIds(@Param("ids") List<Long> ids);
	
	Nutrition findById(Long id);
	
	List<Nutrition> findAll();
	
	List<Nutrition> searchByCategory(@Param("category") FoodCategory category, @Param("offset") int offset, @Param("limit") int limit);
	
	long countByCategory(@Param("category") FoodCategory category);
}
