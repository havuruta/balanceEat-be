package com.balanceeat.demo.domain.auth.exception;

import org.springframework.http.HttpStatus;

import com.balanceeat.demo.exception.ErrorMessage;

public class UserAlreadyExistsException extends AuthenticationException {
    public UserAlreadyExistsException() {
        super(ErrorMessage.USER_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
    }
} 