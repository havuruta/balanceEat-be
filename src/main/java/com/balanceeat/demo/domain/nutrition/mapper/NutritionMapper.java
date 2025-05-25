package com.balanceeat.demo.domain.nutrition.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.entity.FoodCategory;

@Mapper
public interface NutritionMapper {
	
	List<Nutrition> selectAll(@Param("offset") int offset, @Param("limit") int limit);
	
	long countAll();
	
	Nutrition selectById(Long id);
	
	List<Nutrition> searchByName(@Param("name") String name, @Param("offset") int offset, @Param("limit") int limit);
	
	long countByName(@Param("name") String name);
	
	List<Nutrition> findByIds(@Param("ids") List<Long> ids);
	
	List<Nutrition> searchByCategory(@Param("category") String category, @Param("offset") int offset, @Param("limit") int limit);
	
	long countByCategory(@Param("category") String category);
}
