package com.balanceeat.demo.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.balanceeat.demo.exception.ErrorMessage;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(ErrorMessage.USER_NOT_FOUND);
    }

    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
} 