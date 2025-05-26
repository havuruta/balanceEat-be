package com.balanceeat.demo.domain.ai.facade;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balanceeat.demo.domain.ai.service.OneTimeChatService;
import com.balanceeat.demo.domain.diet.dto.ai.UserDietDTO;
import com.balanceeat.demo.domain.diet.service.DietScoreService;
import com.balanceeat.demo.domain.diet.service.DietSummaryService;
import com.balanceeat.demo.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * AI 분석과 식단 점수 저장을 조정하는 Facade 서비스
 */
@Service
@RequiredArgsConstructor
public class DietAnalysisFacade {
    
    private final UserService userService;
    private final DietSummaryService dietSummaryService;
    private final OneTimeChatService oneTimeChatService;
    private final DietScoreService dietScoreService;

    /**
     * 식단 분석 요청을 처리하고 결과를 저장합니다.
     */
    @Transactional
    public Mono<Void> processDietAnalysis(Long userId, LocalDate date) {
        // 1. 식단 데이터 조회
        UserDietDTO userDietDTO = createUserDietDTO(date, userId);
        
        // 2. AI 분석 수행 및 결과 저장
        return oneTimeChatService.ask(userDietDTO)
            .doOnNext(scoreResult -> {
                scoreResult.setUserId(userId);
                scoreResult.setDate(date);
                dietScoreService.saveScore(scoreResult);
            })
            .then();
    }

    /**
     * AI 분석을 위한 사용자 식단 데이터를 생성합니다.
     * @param date 조회할 날짜
     * @param userId 사용자 ID
     * @return AI 분석용 사용자 식단 데이터
     */
    private UserDietDTO createUserDietDTO(LocalDate date, Long userId) {
        return UserDietDTO.builder()
                .userResponseDTO(userService.getUserResponseDTO(userId))
                .dietSummaryDTO(dietSummaryService.getSummaryDTOByDate(userId, date))
                .build();
    }
} 