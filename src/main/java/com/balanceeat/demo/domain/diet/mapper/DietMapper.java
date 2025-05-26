package com.balanceeat.demo.domain.diet.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.balanceeat.demo.domain.diet.entity.Diet;

@Mapper
public interface DietMapper {
    void insert(Diet diet);
    void update(Diet diet);
    void delete(Long id);
    Optional<Diet> findById(Long id);
    List<Diet> findByDate(Long userId, LocalDate date);
    void batchInsert(List<Diet> diets);
} 