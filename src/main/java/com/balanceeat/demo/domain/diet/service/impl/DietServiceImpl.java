package com.balanceeat.demo.domain.diet.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.mapper.DietMapper;
import com.balanceeat.demo.domain.diet.service.DietService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DietServiceImpl implements DietService {

    private final DietMapper dietMapper;

    @Override
    public List<DietSummary> getDietSummariesByDateRange(Long userId, LocalDate start, LocalDate end) {
        log.debug("식단 요약 조회: 사용자 ID={}, 기간={} ~ {}", userId, start, end);
        return dietMapper.findDietSummariesByDateRange(userId, start, end);
    }

    @Override
    public void addDiet(Diet diet) {
        dietMapper.insertDiet(diet);
    }

    @Override
    public void updateDiet(Diet diet) {
        log.info("식단 수정 서비스 호출: {}", diet);
        dietMapper.updateDiet(diet);
        log.info("식단 수정 완료");
    }

    @Override
    public void deleteDiet(Long id) {
        dietMapper.deleteDiet(id);
    }

    @Override
    public List<Diet> getDietsByDate(Long userId, LocalDate date) {
        return dietMapper.findDietsByDate(userId, date);
    }
} 