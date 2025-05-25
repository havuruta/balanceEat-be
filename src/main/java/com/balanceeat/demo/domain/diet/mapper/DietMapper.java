package com.balanceeat.demo.domain.diet.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;

@Mapper
public interface DietMapper {
    List<DietSummary> findDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end);
    void insertDiet(Diet diet);
    void updateDiet(Diet diet);
    void deleteDiet(Long id);
    List<Diet> findDietsByDate(Long userId, LocalDate date);
    void batchInsert(List<Diet> diets);
    void insert(Diet diet);
    void update(Diet diet);
    Optional<Diet> findById(Long id);
    List<Diet> findByDate(Long userId, java.time.LocalDate date);
    void delete(Long id);
} 