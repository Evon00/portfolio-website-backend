package com.example.portfolio_website_backend.common.exception;

/*
예외 코드 반환 형태
 */
public record ExceptionResponse(
        int code,
        String message
) {
}
