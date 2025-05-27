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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "One-Time Chat", description = "일회성 식단 분석 채팅 API")
@SecurityRequirement(name = "Bearer Authentication")
public class OneTimeChatController {
    
    private final DietAnalysisFacade dietAnalysisFacade;
    
    @Operation(
        summary = "식단 분석 요청",
        description = "특정 날짜의 식단을 비동기적으로 분석합니다. 분석이 완료되면 완료 메시지를 반환합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "식단 분석 요청 성공",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "해당 날짜의 식단을 찾을 수 없음"
        )
    })
    @GetMapping("/chat")
    public Mono<ResponseEntity<String>> getDietAnalysis(
        @Parameter(description = "분석할 날짜 (ISO 형식: YYYY-MM-DD)", required = true)
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return dietAnalysisFacade.processDietAnalysis(userPrincipal.getId(), date)
            .then(Mono.just(ResponseEntity.ok("식단 분석이 완료되었습니다.")));
    }
    
    // @GetMapping("/chat")
    // public Mono<ResponseEntity<String>> getDietAnalysisWithBlocking(
    //     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
    //     @AuthenticationPrincipal UserPrincipal userPrincipal
    // ) {
    //     return dietAnalysisFacade.processDietAnalysisWithBlocking(userPrincipal.getId(), date)
    //         .then(Mono.just(ResponseEntity.ok("식단 분석이 완료되었습니다.")));
    // }
    
}
