package com.balanceeat.demo.domain.auth.service;

import org.springframework.transaction.annotation.Transactional;

import com.balanceeat.demo.domain.auth.dto.LoginRequestDTO;
import com.balanceeat.demo.domain.auth.dto.RegisterRequestDTO;
import com.balanceeat.demo.domain.user.dto.UserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserResponseDTO register(RegisterRequestDTO registerRequestDTO);
    void login(LoginRequestDTO loginRequestDTO, HttpServletResponse response);
    @Transactional
    void logout(HttpServletRequest request, HttpServletResponse response);
    @Transactional
    void reissue(HttpServletRequest request, HttpServletResponse response);
}