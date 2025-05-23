package com.balanceeat.demo.domain.auth.dto;

import com.balanceeat.demo.domain.user.entity.Gender;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class RegisterRequestDTO {
    private final String email;
    private final String password;
    private String passwordConfirm;
    private final String nickname;
    private int birthYear;
    private final Gender gender;
    private int weight;
    private int height;
    private String diseaseCode;
    private String dietHabit;
    private String foodBlacklist;
    private String foodPreference;
} 