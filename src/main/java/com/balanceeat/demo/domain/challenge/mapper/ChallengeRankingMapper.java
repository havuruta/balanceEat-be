package com.balanceeat.demo.domain.challenge.mapper;

import com.balanceeat.demo.domain.challenge.dto.ChallengeRankDto;
import com.balanceeat.demo.domain.challenge.entity.ChallengeRanking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ChallengeRankingMapper {

    List<ChallengeRankDto> getTopRankings(@Param("recordedDate") LocalDate recordedDate);

    ChallengeRankDto getMyRanking(@Param("recordedDate") LocalDate recordedDate,
                                  @Param("userId") Long userId);
}
