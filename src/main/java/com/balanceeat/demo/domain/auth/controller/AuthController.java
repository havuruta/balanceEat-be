package com.balanceeat.demo.domain.auth.controller;

import com.balanceeat.demo.domain.auth.dto.LoginRequestDTO;
import com.balanceeat.demo.domain.auth.dto.RegisterRequestDTO;
import com.balanceeat.demo.domain.auth.service.AuthService;
import com.balanceeat.demo.domain.user.dto.UserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "로그인",
        description = "사용자 이메일과 비밀번호로 로그인합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @Parameter(description = "로그인 요청 정보", required = true)
        @RequestBody LoginRequestDTO loginRequestDTO,
        HttpServletResponse response) {
        authService.login(loginRequestDTO, response);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "로그아웃",
        description = "현재 로그인된 사용자를 로그아웃합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "로그아웃 성공"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        )
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
        @Parameter(description = "HTTP 요청", hidden = true)
        HttpServletRequest request,
        @Parameter(description = "HTTP 응답", hidden = true)
        HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "회원가입",
        description = "새로운 사용자를 등록합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "이미 존재하는 이메일"
        )
    })
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> register(
        @Parameter(description = "회원가입 요청 정보", required = true)
        @RequestBody RegisterRequestDTO registerRequestDTO) {
        UserResponseDTO userResponseDTO = authService.register(registerRequestDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(
        summary = "토큰 재발급",
        description = "액세스 토큰이 만료된 경우 리프레시 토큰을 사용하여 새로운 토큰을 발급받습니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "토큰 재발급 성공"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "유효하지 않은 리프레시 토큰"
        )
    })
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
        @Parameter(description = "HTTP 요청", hidden = true)
        HttpServletRequest request,
        @Parameter(description = "HTTP 응답", hidden = true)
        HttpServletResponse response) {
        authService.reissue(request, response);
        return ResponseEntity.ok().build();
    }
} 