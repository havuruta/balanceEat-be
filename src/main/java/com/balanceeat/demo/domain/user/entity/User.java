package com.balanceeat.demo.domain.user.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    
    // 유저 정보
    private String nickname;
    private int birthYear;
    private Gender gender;
    private int weight;
    private int height;
    private String diseaseCode;
    private String dietHabit;
    private String foodBlacklist;
    private String foodPreference;
    
    // audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 회원 탈퇴
    private boolean isActive = true;  // 기본값은 true로 설정
    
    // 로그인 시도 관련 필드
    private int loginAttempts = 0;
    private LocalDateTime lastLoginAttempt = LocalDateTime.now();
    private LocalDateTime passwordExpiryDate;
    
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.lastLoginAttempt = null;
    }
    
    public void loginFailed() {
        this.loginAttempts++;
        this.lastLoginAttempt = LocalDateTime.now();
    }
} 