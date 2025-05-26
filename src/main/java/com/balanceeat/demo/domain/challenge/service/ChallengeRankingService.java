package com.balanceeat.demo.domain.challenge.service;

import com.balanceeat.demo.domain.challenge.dto.ChallengeRankingResponseDto;

public interface ChallengeRankingService {
    ChallengeRankingResponseDto getWeeklyRankingsWithMyRank(Long id);
}
