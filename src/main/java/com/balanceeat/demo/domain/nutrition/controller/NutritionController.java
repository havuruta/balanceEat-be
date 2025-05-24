package com.balanceeat.demo.domain.nutrition.controller;

import com.balanceeat.demo.domain.nutrition.dto.NutritionResponseDTO;
import com.balanceeat.demo.domain.nutrition.dto.PageResponseDTO;
import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.service.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nutrition")
@Tag(name = "Nutrition API", description = "영양 정보 관리를 위한 API")
@RequiredArgsConstructor
public class NutritionController {

    private static final Logger logger = LoggerFactory.getLogger(NutritionController.class);
    private final NutritionService nutritionService;

    @GetMapping
    @Operation(summary = "영양 정보 목록 조회", description = "모든 영양 정보를 페이지네이션과 함께 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<PageResponseDTO<NutritionResponseDTO>> getAllNutritions(
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size) {
        logger.info("영양 정보 목록 조회 요청 - 페이지: {}, 크기: {}", page, size);
        PageResponseDTO<NutritionResponseDTO> response = nutritionService.getAllNutritions(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "영양 정보 검색", description = "이름 또는 카테고리로 영양 정보를 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "검색 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<PageResponseDTO<NutritionResponseDTO>> searchNutritions(
            @Parameter(description = "검색어") @RequestParam(required = false) String name,
            @Parameter(description = "카테고리") @RequestParam(required = false) String category,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size) {
        logger.info("영양 정보 검색 요청 - 이름: {}, 카테고리: {}, 페이지: {}, 크기: {}", name, category, page, size);
        PageResponseDTO<NutritionResponseDTO> response = nutritionService.searchNutritions(name, category, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "영양 정보 조회", description = "ID로 특정 영양 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "찾을 수 없음")
    })
    public ResponseEntity<NutritionResponseDTO> getNutrition(
            @Parameter(description = "영양 정보 ID") @PathVariable Long id) {
        try {
            Nutrition nutrition = nutritionService.getNutritionById(id);
            return ResponseEntity.ok(NutritionResponseDTO.from(nutrition));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 