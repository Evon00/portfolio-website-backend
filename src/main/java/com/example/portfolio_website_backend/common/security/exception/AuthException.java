package com.example.portfolio_website_backend.common.security.exception;

import com.example.portfolio_website_backend.common.exception.BusinessException;
import com.example.portfolio_website_backend.common.exception.ExceptionCode;

/*
인증 관련 Exception 반환 형태
- [param] : 예외코드
- [return] : 예외코드를 포함한 반환 형태
 */
public class AuthException extends BusinessException {

    public AuthException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
