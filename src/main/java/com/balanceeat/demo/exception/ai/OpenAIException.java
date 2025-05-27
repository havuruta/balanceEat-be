package com.balanceeat.demo.exception.ai;

public class OpenAIException extends ChatException {
    public OpenAIException(String message) {
        super(message);
    }

    public OpenAIException(String message, Throwable cause) {
        super(message, cause);
    }
} 