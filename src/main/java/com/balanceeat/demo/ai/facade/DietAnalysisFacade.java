package com.balanceeat.demo.ai.facade;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balanceeat.demo.ai.service.OneTimeChatService;
import com.balanceeat.demo.domain.diet.dto.ai.UserDietDTO;
import com.balanceeat.demo.domain.diet.service.DietService;
import com.balanceeat.demo.domain.diet.service.DietSummaryService;
import com.balanceeat.demo.domain.dietScoreResult.service.DietScoreResultService;
import com.balanceeat.demo.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * AI 분석과 식단 점수 저장을 조정하는 Facade 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DietAnalysisFacade {
    
    private final UserService userService;
     private final OneTimeChatService oneTimeChatService;
    private final DietScoreResultService dietScoreResultService;
    private final DietSummaryService dietSummaryService;
    
    /**
     * 식단 분석 요청을 처리하고 결과를 저장합니다.
     */
    @Transactional
    public Mono<Void> processDietAnalysis(Long userId, LocalDate date) {
        // 1. 식단 데이터 조회
        UserDietDTO userDietDTO = createUserDietDTO(userId, date);
        
        // 2. AI 분석 수행 및 결과 저장
        return oneTimeChatService.ask(userDietDTO)
            .doOnNext(sr -> {
                sr.setUserId(userId);
                sr.setDate(date);
                dietScoreResultService.saveScore(sr);
            })
            .then();
    }
    /**
     * AI 분석을 위한 사용자 식단 데이터를 생성합니다.
     * @param date 조회할 날짜
     * @param userId 사용자 ID
     * @return AI 분석용 사용자 식단 데이터
     */
    private UserDietDTO createUserDietDTO(Long userId, LocalDate date) {
        return UserDietDTO.builder()
                .userResponseDTO(userService.getUserResponseDTO(userId))
                .dietSummaryDTO(dietSummaryService.getSummaryDTOByDate(userId, date))
                .build();
    }
}
