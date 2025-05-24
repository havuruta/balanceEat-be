package com.balanceeat.demo.domain.diet.mapper;

import com.balanceeat.demo.domain.diet.entity.DietSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DietSummaryMapper {
    DietSummary findByDateAndUserId(@Param("date") LocalDate date, @Param("userId") Long userId);
    List<DietSummary> findByDateRange(@Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end);
    void insert(DietSummary summary);
    void update(DietSummary summary);
} 