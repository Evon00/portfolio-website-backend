package com.example.portfolio_website_backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 수정 요청 DTO")
public record MemberUpdateRequestDTO(
        @Schema(description = "사용자 이름", example = "수정된 관리자")
        String name,
        @Schema(description = "사용자 정보", example = "수정된 관리자입니다.")
        String description,
        @Schema(description = "사용자 깃헙주소", example = "수정된 깃헙주소")
        String githubUrl,
        @Schema(description = "사용자 이메일주소", example = "수정된 이메일주소")
        String emailUrl,
        @Schema(description = "사용자 프로필 이미지 URL", example = "수정된 URL")
        String profileUrl,
        @Schema(description = "사용자 프로필 이미지 S3Key", example = "수정된 S3Key")
        String s3Key
) {
}
