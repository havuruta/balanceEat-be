package com.balanceeat.demo.domain.dietScoreResult.controller;

import java.time.LocalDate;

import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.domain.dietScoreResult.entity.DietScoreResult;
import com.balanceeat.demo.domain.dietScoreResult.service.DietScoreResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diet-score")
@RequiredArgsConstructor
public class DietScoreResultController {
    
    private final DietScoreResultService dietScoreResultService;
    
    @GetMapping
    public ResponseEntity<DietScoreResult> getDietScoreResult(
        @RequestParam LocalDate date,
        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        DietScoreResult result = dietScoreResultService.getDietScoreResult(userPrincipal.getId(), date);
        return ResponseEntity.ok(result);
    }
} 