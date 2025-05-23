package com.balanceeat.demo.domain.nutrition.exception;

import org.springframework.http.HttpStatus;

public class NutritionNotFoundException extends RuntimeException {
    public NutritionNotFoundException() {
        super("영양 정보를 찾을 수 없습니다.");
    }

    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
} 