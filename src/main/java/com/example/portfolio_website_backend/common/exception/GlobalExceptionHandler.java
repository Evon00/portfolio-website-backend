package com.example.portfolio_website_backend.common.exception;

import com.example.portfolio_website_backend.common.security.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /*
    기본적인 모든 오류 코드에 대한 반환
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleException(final BusinessException e) {
        log.error(e.getMessage());
        HttpStatusCode httpStatus = e.getStatus();

        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getErrorCode(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }

    /*
    Auth와 관련된 오류 코드에 대한 반환
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponse> handleAuthException(final AuthException e) {
        log.error(e.getMessage());
        HttpStatusCode httpStatus = e.getStatus();
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }

    /*
    부적절한 상태에 대한 오류 코드 반환
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalStateException(final IllegalStateException e) {
        log.error(e.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse(1006, "요청에 대한 권한이 없습니다.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }

    /*
    요청 바디가 없거나, 형식이 잘못되었을때 오류 코드 반환
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        ExceptionResponse response = new ExceptionResponse(1007, "요청 본문이 비어있거나 잘못된 형식입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /*
    기본적인 모든 오류에 대한 오류 코드 반환
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(Exception e) {
        log.error("Unhandled exception", e);
        ExceptionResponse response = new ExceptionResponse(9999, "서버 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
