package com.balanceeat.demo.global.exception;

public class DietNotFoundException extends RuntimeException {
    public DietNotFoundException(String message) {
        super(message);
    }
} 