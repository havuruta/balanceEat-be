package com.balanceeat.demo.domain.dietScoreResult.service;

import java.time.LocalDate;

import com.balanceeat.demo.domain.dietScoreResult.entity.DietScoreResult;
import com.balanceeat.demo.domain.dietScoreResult.mapper.DietScoreResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface DietScoreResultService {
    
    DietScoreResult getDietScoreResult(Long userId, LocalDate date);
    
    @Transactional
    void saveScore(DietScoreResult dietScoreResult);
    
}