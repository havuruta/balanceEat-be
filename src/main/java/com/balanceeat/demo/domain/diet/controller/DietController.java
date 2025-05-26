package com.balanceeat.demo.domain.diet.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.service.DietService;
import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietDetailResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api/diet")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    @GetMapping("/summaries")
    @ResponseBody
    public ResponseEntity<List<DietSummaryDTO>> getDietSummaries(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDate start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDate end,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<DietSummaryDTO> summaries = dietService.getDietSummariesByDateRange(userPrincipal.getId(), start, end);
        return ResponseEntity.ok().body(summaries);
    }
    
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addDiet(@RequestBody Map<String, Object> dietData,
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            // 식단 데이터 생성
            Diet diet = new Diet();
            // 식단 추가
            dietService.addDiet(diet);
            
            return ResponseEntity.ok().body("식단이 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("식단 추가 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/batch-add")
    @ResponseBody
    public ResponseEntity<?> batchAddDiets(@RequestBody DietAddRequestDTO request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            log.info("식단 일괄 추가 요청 수신: {}", request);
            Long userId = userPrincipal.getId();
            
            dietService.batchAddDiets(request, userId);
            return ResponseEntity.ok().body("식단이 일괄 추가되었습니다.");
        } catch (Exception e) {
            log.error("식단 일괄 추가 중 오류 발생", e);
            return ResponseEntity.badRequest().body("식단 일괄 추가 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateDiet(@PathVariable Long id, @RequestBody Map<String, Object> dietData) {
        try {
            log.info("식단 수정 요청 수신: ID={}, 데이터={}", id, dietData);
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
                log.warn("로그인되지 않은 사용자가 식단 수정을 시도했습니다.");
                return ResponseEntity.status(401).body("로그인이 필요합니다.");
            }
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getId();
            
            Diet diet = new Diet();
            
            log.info("수정할 식단 정보: {}", diet);
            return ResponseEntity.ok().body("식단이 수정되었습니다.");
        } catch (Exception e) {
            log.error("식단 수정 중 오류 발생", e);
            return ResponseEntity.badRequest().body("식단 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<Diet>> getDietsByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
                return ResponseEntity.status(401).body(null);
            }
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getId();
            
            List<Diet> diets = dietService.getDietsByDate(userId, date);
            return ResponseEntity.ok().body(diets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteDiet(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
                return ResponseEntity.status(401).body("로그인이 필요합니다.");
            }
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getId();
            
            dietService.deleteDiet(id);
            return ResponseEntity.ok().body("식단이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("식단 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/edit")
    @ResponseBody
    public ResponseEntity<List<Diet>> getDietsForEdit(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
                return ResponseEntity.status(401).body(null);
            }
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getId();
            
            log.info("식단 조회 요청: 날짜={}, 사용자 ID={}", date, userId);
            List<Diet> diets = dietService.getDietsByDate(userId, date);
            log.info("조회된 식단 수: {}", diets.size());
            
            return ResponseEntity.ok().body(diets);
        } catch (Exception e) {
            log.error("식단 조회 중 오류 발생", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/detail")
    @ResponseBody
    public ResponseEntity<DietDetailResponse> getDietDetail(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        DietDetailResponse response = dietService.getDietDetailByDate(date, userPrincipal.getId());
        return ResponseEntity.ok(response);
    }
} 