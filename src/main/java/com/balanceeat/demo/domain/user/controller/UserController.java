package com.balanceeat.demo.domain.user.controller;

import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.dto.UserProfileDTO;
import com.balanceeat.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // 의존성 주입
    private final UserService userService;

    // 현재 로그인한 사용자의 프로필 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            UserProfileDTO profile = userService.getCurrentUserProfile(userDetails);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            log.error("사용자 프로필 조회 중 오류 발생", e);
            return ResponseEntity.status(500)
                    .body(UserProfileDTO.builder()
                            .id(null)
                            .nickname("Error")
                            .profileImageUrl(null)
                            .goalMessage(e.getMessage())
                            .build());
        }
    }

    // 사용자 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) { // @PathVariable : URL 경로 안의 값을 메서드 파라미터로 바인딩해주는 어노테이션
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 특정 사용자의 프로필 정보 조회
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        UserProfileDTO profile = userService.getUserProfile(id);
        return ResponseEntity.ok(profile);
    }

    // 사용자 정보 수정
    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDto, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UserDTO updatedUser = userService.updateUser(userDto, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 사용자 탈퇴
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
} 