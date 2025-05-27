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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/diet")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;
    private final DietSummaryService dietSummaryService;

    @GetMapping("/summaries")
    public ResponseEntity<List<DietSummaryDTO>> getDietSummaries(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDate start,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDate end,
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<DietSummaryDTO> summaries = dietService.getDietSummariesByDateRange(userPrincipal.getId(), start, end);
        return ResponseEntity.ok().body(summaries);
    }
    
    @PostMapping("/batch-add")
    public ResponseEntity<?> batchAddDiets(@RequestBody DietAddRequestDTO request,
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        dietService.batchAddDiets(request, userPrincipal.getId());
        return ResponseEntity.ok().body("식단이 일괄 추가되었습니다.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDiet(@PathVariable Long id,
        @RequestBody DietUpdateRequestDTO dietUpdateRequestDTO,
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        dietService.updateDiet(id, dietUpdateRequestDTO, userPrincipal.getId());
        return ResponseEntity.ok().body("식단이 수정되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<DietDTO>> getDietsByDate(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok(dietService.getDietsByDate(userPrincipal.getId(), date));
    }
    
    @GetMapping("/detail")
    public ResponseEntity<DietDetailResponse> getDietDetail(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        DietDetailResponse response = dietService.getDietDetailByDate(userPrincipal.getId(), date);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDiet(@PathVariable Long id,
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        dietService.deleteDiet(id, userPrincipal.getId());
        return ResponseEntity.ok().body("식단이 삭제되었습니다.");
    }
} 