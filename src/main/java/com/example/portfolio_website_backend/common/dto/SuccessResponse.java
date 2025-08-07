package com.example.portfolio_website_backend.common.dto;

/*
요청에 대한 올바른 응답시 반환 형태
 */
public record SuccessResponse<T>(
        int code,
        String message,
        T data
) {
    public static <T> SuccessResponse<T> of (int code, String message, T data){
        return new SuccessResponse<>(code, message, data);
    }

    public static <T> SuccessResponse<T> ok (T data){
        return new SuccessResponse<>(200,"요청이 성공적으로 처리되었습니다.",data);
    }
}
