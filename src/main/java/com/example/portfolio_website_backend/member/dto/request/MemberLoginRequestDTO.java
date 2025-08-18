package com.example.portfolio_website_backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 로그인 요청 DTO")
public record MemberLoginRequestDTO(
        @Schema(description = "사용자 ID", example = "admin")
        String username,

        @Schema(description = "사용자 PW", example = "admin123")
        String password
) {
}
