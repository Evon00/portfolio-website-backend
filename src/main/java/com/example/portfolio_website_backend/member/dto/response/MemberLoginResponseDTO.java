package com.example.portfolio_website_backend.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 로그인 응답 DTO")
public record MemberLoginResponseDTO(

        @Schema(description = "JWT 토큰", example = "JWT 토큰")
        String jwt
) {
}
