package com.example.portfolio_website_backend.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/*
요청에 대한 올바른 응답시 반환 형태
 */
@Schema(description = "API 정상 응답시 반환 DTO")
public record SuccessResponse<T>(

        @Schema(description = "응답코드", example = "200")
        int code,
        @Schema(description = "응답 메세지", example = "요청이 성공적으로 처리되었습니다.")
        String message,
        @Schema(description = "응답 성공시 반환할 데이터")
        T data
) {
    public static <T> SuccessResponse<T> of (int code, String message, T data){
        return new SuccessResponse<>(code, message, data);
    }

    public static <T> SuccessResponse<T> ok (T data){
        return new SuccessResponse<>(200,"요청이 성공적으로 처리되었습니다.",data);
    }
}
