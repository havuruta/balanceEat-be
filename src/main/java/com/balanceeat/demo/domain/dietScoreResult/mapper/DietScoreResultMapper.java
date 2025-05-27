package com.balanceeat.demo.domain.dietScoreResult.mapper;

import java.time.LocalDate;

import com.balanceeat.demo.domain.dietScoreResult.entity.DietScoreResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DietScoreResultMapper {
    
    int upsert(DietScoreResult dietScoreResult);
    
    DietScoreResult findByUserIdAndDate(Long userId, LocalDate date);
} 