package com.balanceeat.demo.domain.user.controller;

import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.dto.UserProfileDTO;
import com.balanceeat.demo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User", description = "사용자 프로필 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Operation(
        summary = "현재 사용자 프로필 조회",
        description = "현재 로그인한 사용자의 프로필 정보를 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "프로필 조회 성공",
            content = @Content(schema = @Schema(implementation = UserProfileDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류"
        )
    })
    @GetMapping
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile(
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401).build();
            }
            UserProfileDTO profile = userService.getCurrentUserProfile(userDetails);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            log.error("사용자 프로필 조회 중 오류 발생", e);
            return ResponseEntity.status(500)
                    .body(UserProfileDTO.builder()
                            .nickname("Error")
                            .profileImageUrl(null)
                            .goalMessage(e.getMessage())
                            .build());
        }
    }

    @Operation(
        summary = "프로필 정보 수정",
        description = "현재 로그인한 사용자의 프로필 정보를 수정합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "프로필 수정 성공",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        )
    })
    @PutMapping
    public ResponseEntity<UserDTO> updateProfile(
        @Parameter(description = "수정할 사용자 정보", required = true)
        @RequestBody UserDTO userDto,
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            UserDTO updatedUser = userService.updateUser(userDto, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("프로필 정보 업데이트 중 오류 발생", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "회원 탈퇴",
        description = "현재 로그인한 사용자의 계정을 비활성화합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "회원 탈퇴 성공"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류"
        )
    })
    @DeleteMapping
    public ResponseEntity<?> deleteProfile(
        @Parameter(description = "인증된 사용자 정보", hidden = true)
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        userService.deleteUser(userPrincipal.getId());
        return ResponseEntity.ok().build();
    }
} 