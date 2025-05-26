package com.balanceeat.demo.domain.challenge.service;

import com.balanceeat.demo.domain.challenge.dto.ChallengeRankDto;
import com.balanceeat.demo.domain.challenge.dto.ChallengeRankingResponseDto;
import com.balanceeat.demo.domain.challenge.mapper.ChallengeRankingMapper;
import com.balanceeat.demo.exception.BusinessException;
import com.balanceeat.demo.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeRankingServiceImpl implements ChallengeRankingService {

    private final ChallengeRankingMapper rankingMapper;

    @Override
    public ChallengeRankingResponseDto getWeeklyRankingsWithMyRank(Long id) {
        LocalDate now = LocalDate.now();

        // 1. 이번 주 TOP 10
        List<ChallengeRankDto> topRankings = rankingMapper.getTopRankings(now);
        // 2. 내 랭킹
        ChallengeRankDto myRanking = rankingMapper.getMyRanking(now, id);

        if (myRanking == null) {
            throw new BusinessException(ErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        // 결과 반환
        return ChallengeRankingResponseDto.builder()
                .topRankers(topRankings)
                .myRank(myRanking)
                .build();
    }
}
