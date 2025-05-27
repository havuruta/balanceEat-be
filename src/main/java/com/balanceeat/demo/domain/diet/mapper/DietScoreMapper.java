package com.balanceeat.demo.domain.diet.mapper;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

import com.balanceeat.demo.domain.dietScoreResult.entity.DietScoreResult;

@Mapper
public interface DietScoreMapper {
    
    /**
     * AI 분석 결과를 저장하거나 업데이트합니다.
     * @param dietScoreResult 저장할 분석 결과
     * @return 영향받은 행의 수
     */
    int upsert(DietScoreResult dietScoreResult);
    
    /**
     * 특정 사용자의 특정 날짜의 식단 점수를 조회합니다.
     * @param userId 사용자 ID
     * @param date 조회할 날짜
     * @return 식단 점수 결과
     */
    DietScoreResult findByUserIdAndDate(Long userId, LocalDate date);
} 