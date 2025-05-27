package com.balanceeat.demo.domain.challenge.controller;

import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.domain.challenge.dto.ChallengeRankingResponseDto;
import com.balanceeat.demo.domain.challenge.service.ChallengeRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenge/rankings")
@RequiredArgsConstructor
@Tag(name = "Challenge Ranking API", description = "챌린지 랭킹 조회 API")
public class ChallengeRankingController {

    private final ChallengeRankingService rankingService;

    @GetMapping
    @Operation(summary = "챌린지 랭킹 조회", description = "상위 10명과 내 랭킹 반환")
    public ResponseEntity<ChallengeRankingResponseDto> getWeeklyRankings(
            @AuthenticationPrincipal UserPrincipal user) {

        Long id = user.getId();
        ChallengeRankingResponseDto response = rankingService.getWeeklyRankingsWithMyRank(id);
        return ResponseEntity.ok(response);
    }

}
