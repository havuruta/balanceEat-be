package com.balanceeat.demo.exception;

import com.balanceeat.demo.exception.auth.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorMessage> handleBusinessException(BusinessException e) {
        log.error("Business Exception: {}", e.getMessage());
        return new ResponseEntity<>(e.toErrorMessage(), e.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Data Integrity Violation: {}", e.getMessage());
        String errorMessage = e.getMessage();
        String customMessage;
        
        if (errorMessage.contains("Data too long")) {
            customMessage = ErrorMessage.DATA_TOO_LONG;
        } else if (errorMessage.contains("cannot be null")) {
            customMessage = ErrorMessage.REQUIRED_FIELD_MISSING;
        } else {
            customMessage = ErrorMessage.DATA_INTEGRITY_VIOLATION;
        }
        
        BusinessException businessException = new BusinessException(
            customMessage,
            HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(businessException.toErrorMessage(), businessException.getStatus());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("Duplicate Key: {}", e.getMessage());
        BusinessException businessException = new BusinessException(
            ErrorMessage.DUPLICATE_ENTRY,
            HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(businessException.toErrorMessage(), businessException.getStatus());
    }

    @ExceptionHandler(TooManyLoginAttemptsException.class)
    public ResponseEntity<ErrorMessage> handleTooManyLoginAttemptsException(TooManyLoginAttemptsException e) {
        log.error("Too Many Login Attempts: {}", e.getMessage());
        BusinessException businessException = new BusinessException(
            ErrorMessage.TOO_MANY_LOGIN_ATTEMPTS,
            HttpStatus.TOO_MANY_REQUESTS
        );
        return new ResponseEntity<>(businessException.toErrorMessage(), businessException.getStatus());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorMessage> handleUnAuthorizedException(UnAuthorizedException e) {
        log.error("Unauthorized: {}", e.getMessage());
        BusinessException businessException = new BusinessException(
            ErrorMessage.UNAUTHORIZED_EXCEPTION,
            HttpStatus.UNAUTHORIZED
        );
        return new ResponseEntity<>(businessException.toErrorMessage(), businessException.getStatus());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error("User Already Exists: {}", e.getMessage());
        BusinessException businessException = new BusinessException(
            ErrorMessage.USER_ALREADY_EXIST,
            HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(businessException.toErrorMessage(), businessException.getStatus());
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorMessage> handleAccountLockedException(AccountLockedException e) {
        log.error("Account Locked: {}", e.getMessage());
        BusinessException businessException = new BusinessException(
            ErrorMessage.ACCOUNT_LOCKED,
            HttpStatus.LOCKED
        );
        return new ResponseEntity<>(businessException.toErrorMessage(), businessException.getStatus());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorMessage> handleInvalidTokenException(InvalidTokenException e) {
        log.error("Invalid Token: {}", e.getMessage());
        BusinessException businessException = new BusinessException(
            ErrorMessage.INVALID_TOKEN_EXCEPTION,
            HttpStatus.UNAUTHORIZED
        );
        return new ResponseEntity<>(businessException.toErrorMessage(), businessException.getStatus());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        log.error("Media Type Not Acceptable: {}", e.getMessage());
        BusinessException businessException = new BusinessException(
            "응답 형식이 지원되지 않습니다.",
            HttpStatus.NOT_ACCEPTABLE
        );
        return new ResponseEntity<>(businessException.toErrorMessage(), businessException.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        log.error("Unexpected Exception: ", e);
        BusinessException businessException = new BusinessException(
            ErrorMessage.INTERNAL_SERVER_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(businessException.toErrorMessage(), businessException.getStatus());
    }
} 