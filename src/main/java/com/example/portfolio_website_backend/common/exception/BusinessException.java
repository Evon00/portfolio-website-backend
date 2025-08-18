package com.example.portfolio_website_backend.common.exception;

import org.springframework.http.HttpStatus;

/*
BusinessException 반환 형태
*/
public class BusinessException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public BusinessException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public HttpStatus getStatus() {
        return exceptionCode.getHttpStatus();
    }

    public int getErrorCode() {
        return exceptionCode.getErrorCode();
    }
}