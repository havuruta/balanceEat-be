package com.balanceeat.demo.exception.ai;

public class SessionNotFoundException extends ChatException {
    public SessionNotFoundException(String sessionId) {
        super("Chat session not found: " + sessionId);
    }
} 