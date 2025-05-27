package com.balanceeat.demo.domain.dietScoreResult.controller;

import java.time.LocalDate;

import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.domain.dietScoreResult.entity.DietScoreResult;
import com.balanceeat.demo.domain.dietScoreResult.service.DietScoreResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/diet-score")
@RequiredArgsConstructor
@Tag(name = "Diet Score", description = "식단 점수 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
public class DietScoreResultController {
    
    private final DietScoreResultService dietScoreResultService;
    
    @Operation(
        summary = "식단 점수 조회",
        description = "특정 날짜의 식단 점수와 분석 결과를 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "식단 점수 조회 성공",
            content = @Content(schema = @Schema(implementation = DietScoreResult.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "해당 날짜의 식단 점수를 찾을 수 없음"
        )
    })
    @GetMapping
    public ResponseEntity<DietScoreResult> getDietScoreResult(
        @Parameter(description = "조회할 날짜", required = true)
        @RequestParam LocalDate date,
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        DietScoreResult result = dietScoreResultService.getDietScoreResult(userPrincipal.getId(), date);
        return ResponseEntity.ok(result);
    }
} 