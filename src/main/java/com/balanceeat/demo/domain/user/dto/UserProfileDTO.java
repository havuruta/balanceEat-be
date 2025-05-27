package com.balanceeat.demo.domain.user.dto;

import lombok.*;
import com.balanceeat.demo.domain.user.entity.Gender;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String goalMessage;
    private boolean isChallengeEnabled;
    private int birthYear;
    private Gender gender;
    private int weight;
    private int height;
    private String diseaseCode;
    private String dietHabit;
    private String foodBlacklist;
    private String foodPreference;
} 