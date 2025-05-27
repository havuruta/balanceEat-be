package com.balanceeat.demo.ai.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.balanceeat.demo.ai.facade.DietAnalysisFacade;
import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.domain.dietScoreResult.entity.DietScoreResult;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OneTimeChatController {
    
    private final DietAnalysisFacade dietAnalysisFacade;
    
    @GetMapping("/chat")
    public Mono<ResponseEntity<String>> getDietAnalysis(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return dietAnalysisFacade.processDietAnalysis(userPrincipal.getId(), date)
            .then(Mono.just(ResponseEntity.ok("식단 분석이 완료되었습니다.")));
    }
    
    @GetMapping("/chat")
    public Mono<ResponseEntity<String>> getDietAnalysisWithBlocking(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return dietAnalysisFacade.processDietAnalysisWithBlocking(userPrincipal.getId(), date)
            .then(Mono.just(ResponseEntity.ok("식단 분석이 완료되었습니다.")));
    }
    
}
