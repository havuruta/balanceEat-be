package com.balanceeat.demo.domain.nutrition.controller;

import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.service.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/nutrition")
@Tag(name = "Nutrition API", description = "영양 정보 관리를 위한 API")
public class NutritionController {

    private static final Logger logger = LoggerFactory.getLogger(NutritionController.class);
    private final NutritionService nutritionService;

    @Autowired
    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping("/list")
    @Operation(summary = "영양 정보 목록 조회", description = "모든 영양 정보를 조회합니다.")
    public String listNutritions(Model model) {
        logger.info("영양 정보 목록 페이지 요청");
        List<Nutrition> nutritions = nutritionService.getAllNutritions();
        logger.info("목록 조회 - 전체 항목 수: {}", nutritions.size());
        model.addAttribute("nutritions", nutritions);
        return "nutrition/list";
    }

    @GetMapping("/detail/{id}")
    public String getNutritionDetail(@PathVariable Long id, Model model) {
        logger.info("영양 정보 상세 페이지 요청: ID={}", id);
        Nutrition nutrition = nutritionService.getNutritionById(id);
        model.addAttribute("nutrition", nutrition);
        return "nutrition/detail";
    }

    @GetMapping("/create")
    public String showCreateForm() {
        logger.info("영양 정보 생성 페이지 요청");
        return "nutrition/create";
    }

    @GetMapping("/search")
    @Operation(summary = "영양 정보 검색", description = "설명으로 영양 정보를 검색합니다.")
    public Object searchNutritions(@RequestParam(required = false) String description, @RequestParam(required = false) String name, Model model, HttpServletRequest request) {
        logger.info("검색어: {}", name != null ? name : description);
        List<Nutrition> nutritions;
        
        // URL 디코딩 처리
        String decodedName = name != null ? java.net.URLDecoder.decode(name, java.nio.charset.StandardCharsets.UTF_8) : null;
        String decodedDescription = description != null ? java.net.URLDecoder.decode(description, java.nio.charset.StandardCharsets.UTF_8) : null;
        
        if (decodedDescription != null && !decodedDescription.trim().isEmpty()) {
            nutritions = nutritionService.searchByDescription(decodedDescription);
            logger.info("검색 결과 수: {}", nutritions.size());
        } else if (decodedName != null && !decodedName.trim().isEmpty()) {
            nutritions = nutritionService.searchByName(decodedName);
            logger.info("검색 결과 수: {}", nutritions.size());
        } else {
            nutritions = nutritionService.getAllNutritions();
            logger.info("검색어가 없어 전체 목록 조회. 결과 수: {}", nutritions.size());
        }

        model.addAttribute("nutritions", nutritions);
        logger.info("모델에 추가된 데이터 - nutritions 크기: {}", nutritions.size());
        
        // Accept 헤더가 application/json이면 JSON 응답 반환
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            return nutritions;
        }
        
        return "nutrition/list";
    }

    @GetMapping("/all")
    @Operation(summary = "모든 영양 정보 조회", description = "모든 영양 정보를 JSON 형식으로 반환합니다.")
    @ResponseBody
    public List<Nutrition> getAllNutritionsJson() {
        return nutritionService.getAllNutritions();
    }

    @GetMapping("/{id}")
    @Operation(summary = "영양 정보 조회", description = "ID로 특정 영양 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "찾을 수 없음")
    })
    public String getNutrition(@Parameter(description = "영양 정보 ID") @PathVariable Long id, Model model) {
        Nutrition nutrition = nutritionService.getNutritionById(id);
        if (nutrition != null) {
            model.addAttribute("nutrition", nutrition);
            return "nutrition/detail";
        }
        return "redirect:/nutrition/list";
    }
} 