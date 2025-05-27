package com.balanceeat.demo.domain.chat.service;

import com.balanceeat.demo.domain.chat.entity.ChatMessage;
import com.balanceeat.demo.domain.chat.entity.ChatSession;
import com.balanceeat.demo.domain.chat.mapper.ChatMessageMapper;
import com.balanceeat.demo.domain.chat.mapper.ChatSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;

    @Transactional(readOnly = true)
    public ChatSession getSession(String sessionId) {
        return sessionMapper.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("세션을 찾을 수 없습니다: " + sessionId));
    }

    @Transactional
    public ChatSession createSession(Long userId, String title) {
        ChatSession session = new ChatSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setTitle(title);
        session.setUserId(userId);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        
        sessionMapper.insert(session);
        return session;
    }

    @Transactional
    public ChatMessage saveMessage(String sessionId, ChatMessage.MessageRole role, String content) {
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setRole(role);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        
        messageMapper.insert(message);
        return message;
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getMessages(String sessionId) {
        return messageMapper.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }

    @Transactional
    public void deleteSession(String sessionId) {
        messageMapper.deleteBySessionId(sessionId);
        sessionMapper.delete(sessionId);
    }
} 