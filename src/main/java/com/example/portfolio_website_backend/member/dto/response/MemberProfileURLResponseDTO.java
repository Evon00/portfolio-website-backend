package com.example.portfolio_website_backend.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 프로필 이미지 URL 응답 DTO")
public record MemberProfileURLResponseDTO(
        @Schema(description = "사용자 프로필 이미지 URL", example = "https://www.example/example1/example2.jpg")
        String profileUrl,
        @Schema(description = "사용자 프로필 이미지 S3Key", example = "example1/example2.jpg")
        String s3Key
) {
}
