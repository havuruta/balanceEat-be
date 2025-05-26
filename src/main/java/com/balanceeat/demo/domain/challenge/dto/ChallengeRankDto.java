// Top 10 User나 내 순위를 담을 수 있는 DTO
// ChallengeRanking + User + UserChallengeProfile 에서 합쳐진 정보

package com.balanceeat.demo.domain.challenge.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ChallengeRankDto {

    private int ranking;               // 순위
    private Long userId;              // 유저 ID (프로필 이동용)
    private String nickname;          // 유저 닉네임
    private String profileImageUrl;   // 프로필 이미지
    private int score;                // 점수
}
