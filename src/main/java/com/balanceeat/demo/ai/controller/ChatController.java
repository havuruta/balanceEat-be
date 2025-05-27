package com.balanceeat.demo.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.ai.dto.ChatResponseDTO;
import com.balanceeat.demo.ai.service.OneTimeChatService;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "AI 채팅 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
public class ChatController {
    private final OneTimeChatService oneTimeChatService;

    @Operation(
        summary = "채팅 세션 생성",
        description = "새로운 AI 채팅 세션을 생성합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "세션 생성 성공",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        )
    })
    @PostMapping("/session")
    public ResponseEntity<String> createSession(
            @Parameter(description = "채팅 세션 제목 (선택사항)")
            @RequestParam(required = false) String title,
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String sessionId = oneTimeChatService.createSession(userPrincipal.getId(), title);
        return ResponseEntity.ok(sessionId);
    }

    @Operation(
        summary = "채팅 메시지 전송",
        description = "AI에게 메시지를 전송하고 응답을 받습니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "메시지 처리 성공",
            content = @Content(schema = @Schema(implementation = ChatResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "세션을 찾을 수 없음"
        )
    })
    @PostMapping("/message")
    public ResponseEntity<ChatResponseDTO> sendMessage(
            @Parameter(description = "채팅 세션 ID", required = true)
            @RequestParam String sessionId,
            @Parameter(description = "전송할 메시지 내용", required = true)
            @RequestBody String content,
            @Parameter(description = "분석할 월 (선택사항)")
            @RequestParam(required = false) Integer month,
            @Parameter(description = "분석할 주차 (선택사항)")
            @RequestParam(required = false) Integer week,
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ChatResponseDTO response = oneTimeChatService.processMessage(sessionId, content, userPrincipal.getId(), month, week);
        return ResponseEntity.ok(response);
    }
} 