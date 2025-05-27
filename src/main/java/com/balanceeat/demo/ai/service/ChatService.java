package com.balanceeat.demo.ai.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    // private final ChatSessionRepository sessionRepository;
    // private final ChatMessageRepository messageRepository;
    //
    // @Transactional(readOnly = true)
    // public ChatSession getSession(String sessionId) {
    //     return sessionRepository.findById(sessionId)
    //             .orElseThrow(() -> new SessionNotFoundException(sessionId));
    // }
    //
    // @Transactional
    // public ChatSession createSession(String title) {
    //     ChatSession session = new ChatSession();
    //     session.setTitle(title);
    //     return sessionRepository.save(session);
    // }
    //
    // @Transactional
    // public ChatMessage saveMessage(ChatSession session, ChatMessage.MessageRole role, String content) {
    //     ChatMessage message = new ChatMessage();
    //     message.setChatSession(session);
    //     message.setRole(role);
    //     message.setContent(content);
    //     return messageRepository.save(message);
    // }
    //
    // @Transactional(readOnly = true)
    // public List<ChatMessage> getMessages(String sessionId) {
    //     return messageRepository.findByChatSession_SessionIdOrderByCreatedAtAsc(sessionId);
    // }
} 