package com.balanceeat.demo.domain.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AuthenticationException {
    public InvalidCredentialsException() {
        super("아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
} 