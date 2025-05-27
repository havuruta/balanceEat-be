package com.balanceeat.demo.domain.diet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;

@Mapper
public interface DietSummaryMapper {
    List<DietSummaryDTO> findByUserIdAndDateRange(
        @Param("userId") Long userId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );
} 