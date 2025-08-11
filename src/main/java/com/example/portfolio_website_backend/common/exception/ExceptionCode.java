package com.example.portfolio_website_backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    //인증 관련 (1001 ~ 2000)
    EMPTY_ACCESS_TOKEN(1001, BAD_REQUEST, "토큰이 비어있습니다."),
    EXPIRED_TOKEN(1002, UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(1003, BAD_REQUEST, "유효하지 않은 토큰입니다."),
    INVALID_ROLE(1004, BAD_REQUEST, "유효하지 않은 사용자입니다."),
    UNAUTHORIZED_USER(1005, UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    FORBIDDEN_REQUEST(1006, FORBIDDEN, "허가되지 않은 요청입니다."),
    INVALID_DATA(1007,BAD_REQUEST,"요청 본문이 비어있거나 잘못된 형식입니다."),

    //회원 관련 (2001 ~ 3000)
    INVALID_LOGIN_CREDENTIALS(2001, UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    USER_NOT_FOUND(2002, NOT_FOUND, "존재하지 않은 사용자입니다.");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String message;

}
