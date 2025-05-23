package com.balanceeat.demo.domain.diet.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.service.DietService;
import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/diet")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    @GetMapping("/calendar")
    public String calendarPage() {
        return "diet/calendar";
    }

    @GetMapping("/summaries")
    @ResponseBody
    public ResponseEntity<List<DietSummaryDTO>> getDietSummaries(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDate start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDate end,
            HttpSession session) {
        
        // 세션에서 사용자 ID 가져오기
        Map<String, String> userInfo = (Map<String, String>) session.getAttribute("user");
        if (userInfo == null || userInfo.get("id") == null) {
            log.warn("로그인되지 않은 사용자가 식단 요약을 요청했습니다.");
            return ResponseEntity.ok().body(List.of());
        }
        Long userId = Long.parseLong(userInfo.get("id"));
        
        log.info("식단 요약 조회: 사용자 ID={}, 기간={} ~ {}", userId, start, end);
        
        List<DietSummary> summaries = dietService.getDietSummariesByDateRange(userId, start, end);
        List<DietSummaryDTO> dtos = summaries.stream()
                .map(DietSummaryDTO::fromEntity)
                .toList();
        
        return ResponseEntity.ok().body(dtos);
    }
    
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addDiet(@RequestBody Map<String, Object> dietData, HttpSession session) {
        try {
            // 세션에서 사용자 ID 가져오기
            Map<String, String> userInfo = (Map<String, String>) session.getAttribute("user");
            if (userInfo == null || userInfo.get("id") == null) {
                return ResponseEntity.status(401).body("로그인이 필요합니다.");
            }
            Long userId = Long.parseLong(userInfo.get("id"));
            
            // 식단 데이터 생성
            Diet diet = new Diet();
            diet.setUserId(userId);
            diet.setDietDate(LocalDate.parse((String) dietData.get("dietDate")));
            diet.setMealType((String) dietData.get("mealType"));
            diet.setFoodName((String) dietData.get("foodName"));
            diet.setAmount(Double.parseDouble(dietData.get("amount").toString()));
            diet.setCalories(Double.parseDouble(dietData.get("calories").toString()));
            diet.setProtein(Double.parseDouble(dietData.get("protein").toString()));
            diet.setFat(Double.parseDouble(dietData.get("fat").toString()));
            diet.setCarbohydrates(Double.parseDouble(dietData.get("carbohydrates").toString()));
            diet.setNote((String) dietData.get("note"));
            
            
            // 식단 추가
            dietService.addDiet(diet);
            
            return ResponseEntity.ok().body("식단이 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("식단 추가 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateDiet(@PathVariable Long id, @RequestBody Map<String, Object> dietData, HttpSession session) {
        try {
            log.info("식단 수정 요청 수신: ID={}, 데이터={}", id, dietData);
            
            Map<String, String> userInfo = (Map<String, String>) session.getAttribute("user");
            if (userInfo == null || userInfo.get("id") == null) {
                log.warn("로그인되지 않은 사용자가 식단 수정을 시도했습니다.");
                return ResponseEntity.status(401).body("로그인이 필요합니다.");
            }
            
            Diet diet = new Diet();
            diet.setId(id);
            diet.setAmount(Double.parseDouble(dietData.get("amount").toString()));
            
            log.info("수정할 식단 정보: {}", diet);
            dietService.updateDiet(diet);
            return ResponseEntity.ok().body("식단이 수정되었습니다.");
        } catch (Exception e) {
            log.error("식단 수정 중 오류 발생", e);
            return ResponseEntity.badRequest().body("식단 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<Diet>> getDietsByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            HttpSession session) {
        try {
            Map<String, String> userInfo = (Map<String, String>) session.getAttribute("user");
            if (userInfo == null || userInfo.get("id") == null) {
                return ResponseEntity.status(401).body(null);
            }
            Long userId = Long.parseLong(userInfo.get("id"));
            
            List<Diet> diets = dietService.getDietsByDate(userId, date);
            return ResponseEntity.ok().body(diets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteDiet(@PathVariable Long id, HttpSession session) {
        try {
            Map<String, String> userInfo = (Map<String, String>) session.getAttribute("user");
            if (userInfo == null || userInfo.get("id") == null) {
                return ResponseEntity.status(401).body("로그인이 필요합니다.");
            }
            
            dietService.deleteDiet(id);
            return ResponseEntity.ok().body("식단이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("식단 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/edit")
    @ResponseBody
    public ResponseEntity<List<Diet>> getDietsForEdit(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            HttpSession session) {
        try {
            Map<String, String> userInfo = (Map<String, String>) session.getAttribute("user");
            if (userInfo == null || userInfo.get("id") == null) {
                return ResponseEntity.status(401).body(null);
            }
            Long userId = Long.parseLong(userInfo.get("id"));
            
            log.info("식단 조회 요청: 날짜={}, 사용자 ID={}", date, userId);
            List<Diet> diets = dietService.getDietsByDate(userId, date);
            log.info("조회된 식단 수: {}", diets.size());
            
            return ResponseEntity.ok().body(diets);
        } catch (Exception e) {
            log.error("식단 조회 중 오류 발생", e);
            return ResponseEntity.badRequest().body(null);
        }
    }
} 