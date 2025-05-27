package com.balanceeat.demo.domain.diet.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.domain.diet.dto.DietDetailResponse;
import com.balanceeat.demo.domain.diet.dto.ai.UserDietDTO;
import com.balanceeat.demo.domain.diet.service.DietService;
import com.balanceeat.demo.domain.diet.service.DietSummaryService;
import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietDTO;
import com.balanceeat.demo.domain.diet.dto.DietUpdateRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
@Tag(name = "Diet", description = "식단 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
public class DietController {

    private final DietService dietService;
    private final DietSummaryService dietSummaryService;

    @Operation(
        summary = "식단 요약 조회",
        description = "특정 기간 동안의 식단 요약 정보를 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "식단 요약 조회 성공",
            content = @Content(schema = @Schema(implementation = DietSummaryDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        )
    })
    @GetMapping("/summaries")
    public ResponseEntity<List<DietSummaryDTO>> getDietSummaries(
        @Parameter(description = "시작 날짜", required = true)
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDate start,
        @Parameter(description = "종료 날짜", required = true)
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDate end,
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<DietSummaryDTO> summaries = dietService.getDietSummariesByDateRange(userPrincipal.getId(), start, end);
        return ResponseEntity.ok().body(summaries);
    }
    
    @Operation(
        summary = "식단 일괄 추가",
        description = "여러 개의 식단을 한 번에 추가합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "식단 일괄 추가 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        )
    })
    @PostMapping
    public ResponseEntity<?> batchAddDiets(
        @Parameter(description = "추가할 식단 정보", required = true)
        @RequestBody DietAddRequestDTO request,
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        dietService.batchAddDiets(request, userPrincipal.getId());
        return ResponseEntity.ok().body("식단이 일괄 추가되었습니다.");
    }

    @Operation(
        summary = "식단 수정",
        description = "특정 식단의 정보를 수정합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "식단 수정 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "식단을 찾을 수 없음"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiet(
        @Parameter(description = "식단 ID", required = true)
        @PathVariable Long id,
        @Parameter(description = "수정할 식단 정보", required = true)
        @RequestBody DietUpdateRequestDTO dietUpdateRequestDTO,
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        dietService.updateDiet(id, dietUpdateRequestDTO, userPrincipal.getId());
        return ResponseEntity.ok().body("식단이 수정되었습니다.");
    }

    @Operation(
        summary = "날짜별 식단 조회",
        description = "특정 날짜의 식단 목록을 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "식단 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = DietDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        )
    })
    @GetMapping
    public ResponseEntity<List<DietDTO>> getDietsByDate(
        @Parameter(description = "조회할 날짜", required = true)
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok(dietService.getDietsByDate(userPrincipal.getId(), date));
    }
    
    @Operation(
        summary = "식단 상세 조회",
        description = "특정 날짜의 식단 상세 정보를 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "식단 상세 조회 성공",
            content = @Content(schema = @Schema(implementation = DietDetailResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        )
    })
    @GetMapping("/detail")
    public ResponseEntity<DietDetailResponse> getDietDetail(
        @Parameter(description = "조회할 날짜", required = true)
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        DietDetailResponse response = dietService.getDietDetailByDate(userPrincipal.getId(), date);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "식단 삭제",
        description = "특정 식단을 삭제합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "식단 삭제 성공"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "식단을 찾을 수 없음"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiet(
        @Parameter(description = "식단 ID", required = true)
        @PathVariable Long id,
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        dietService.deleteDiet(id, userPrincipal.getId());
        return ResponseEntity.ok().body("식단이 삭제되었습니다.");
    }
} 