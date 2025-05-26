package com.balanceeat.demo.domain.challenge.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ChallengeRanking {

    private Long id;            // PK (자동 증가)
    private Long userId;        // 유저 ID (FK)

    private int score;          // 챌린지 점수
    private int ranking;           // 해당 주차의 순위

    private LocalDate recordedDate; // 랭킹이 기록된 날짜 (주차 구분)

    // 아래는 조인용 필드 (선택)
    private String nickname;          // User 테이블에서 조인
    private String profileImageUrl;   // UserChallengeProfile 테이블에서 조인
}
