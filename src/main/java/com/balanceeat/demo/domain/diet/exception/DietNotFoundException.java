package com.balanceeat.demo.domain.diet.exception;

import org.springframework.http.HttpStatus;

public class DietNotFoundException extends RuntimeException {
    public DietNotFoundException() {
        super("식단을 찾을 수 없습니다.");
    }

    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
} 