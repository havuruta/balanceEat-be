package com.balanceeat.demo.domain.challenge.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChallengeRankingResponseDto {
    private List<ChallengeRankDto> topRankers; // Top 10
    private ChallengeRankDto myRank; // 내 순위
}
