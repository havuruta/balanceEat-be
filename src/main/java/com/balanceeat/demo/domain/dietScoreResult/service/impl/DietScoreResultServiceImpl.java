package com.balanceeat.demo.domain.dietScoreResult.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balanceeat.demo.domain.dietScoreResult.entity.DietScoreResult;
import com.balanceeat.demo.domain.dietScoreResult.mapper.DietScoreResultMapper;
import com.balanceeat.demo.domain.dietScoreResult.service.DietScoreResultService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DietScoreResultServiceImpl implements DietScoreResultService {
	
	private final DietScoreResultMapper dietScoreResultMapper;
	
	/**
	 * 특정 사용자의 특정 날짜의 식단 점수를 조회합니다.
	 * @param userId 사용자 ID
	 * @param date 조회할 날짜
	 * @return 식단 점수 결과
	 */
	@Override
	public DietScoreResult getDietScoreResult(Long userId, LocalDate date) {
		return dietScoreResultMapper.findByUserIdAndDate(userId, date);
	}
	/**
	 * AI 분석 결과를 저장합니다.
	 * @param dietScoreResult 저장할 분석 결과
	 */
	@Transactional
	@Override
	public void saveScore(DietScoreResult dietScoreResult) {
		dietScoreResultMapper.upsert(dietScoreResult);
	}
	
}
