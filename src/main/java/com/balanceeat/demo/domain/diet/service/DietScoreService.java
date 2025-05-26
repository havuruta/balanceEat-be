package com.balanceeat.demo.domain.diet.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balanceeat.demo.domain.ai.entity.DietScoreResult;
import com.balanceeat.demo.domain.diet.mapper.DietScoreMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DietScoreService {

    private final DietScoreMapper dietScoreMapper;

    /**
     * AI 분석 결과를 저장합니다.
     * @param dietScoreResult 저장할 분석 결과
     */
    @Transactional
    public void saveScore(DietScoreResult dietScoreResult) {
        dietScoreMapper.upsert(dietScoreResult);
    }

    /**
     * 특정 사용자의 특정 날짜의 식단 점수를 조회합니다.
     * @param userId 사용자 ID
     * @param date 조회할 날짜
     * @return 식단 점수 결과
     */
    public DietScoreResult getScore(Long userId, LocalDate date) {
        return dietScoreMapper.findByUserIdAndDate(userId, date);
    }
} 