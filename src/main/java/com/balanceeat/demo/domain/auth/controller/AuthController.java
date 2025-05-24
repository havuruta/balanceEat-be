package com.balanceeat.demo.domain.auth.controller;

import com.balanceeat.demo.domain.auth.dto.LoginRequestDTO;
import com.balanceeat.demo.domain.auth.dto.RegisterRequestDTO;
import com.balanceeat.demo.domain.auth.service.AuthService;
import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.dto.UserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        authService.login(loginRequestDTO, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        UserResponseDTO userResponseDTO = authService.register(registerRequestDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        authService.reissue(request, response);
        return ResponseEntity.ok().build();
    }
} 