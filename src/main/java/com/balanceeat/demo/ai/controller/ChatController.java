package com.balanceeat.demo.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.balanceeat.demo.domain.auth.UserPrincipal;
import com.balanceeat.demo.ai.dto.ChatResponseDTO;
import com.balanceeat.demo.ai.service.OneTimeChatService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OneTimeChatService oneTimeChatService;

    @PostMapping("/session")
    public ResponseEntity<String> createSession(
            @RequestParam(required = false) String title,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String sessionId = oneTimeChatService.createSession(userPrincipal.getId(), title);
        return ResponseEntity.ok(sessionId);
    }
    

    @PostMapping("/message")
    public ResponseEntity<ChatResponseDTO> sendMessage(
            @RequestParam String sessionId,
            @RequestBody String content,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer week,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ChatResponseDTO response = oneTimeChatService.processMessage(sessionId, content, userPrincipal.getId(), month, week);
        return ResponseEntity.ok(response);
    }
} 