package com.balanceeat.demo.domain.auth.service;

import org.springframework.transaction.annotation.Transactional;

import com.balanceeat.demo.domain.auth.dto.LoginRequestDTO;
import com.balanceeat.demo.domain.auth.dto.RegisterRequestDTO;
import com.balanceeat.demo.domain.auth.dto.TokenDTO;
import com.balanceeat.demo.domain.user.dto.UserResponseDTO;
import com.balanceeat.demo.domain.user.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    UserResponseDTO register(RegisterRequestDTO registerRequestDTO);
    void login(LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse);
    @Transactional
    void logout(HttpServletRequest request, HttpServletResponse response);
    @Transactional
    TokenDTO.Response reissue(HttpServletRequest request, HttpServletResponse response);
}