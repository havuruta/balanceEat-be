package com.balanceeat.demo.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequestDTO {
    private String email;
    private String password;
} 