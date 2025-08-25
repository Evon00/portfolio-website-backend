package com.example.portfolio_website_backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "회원의 기술 스택 추가 요청 DTO")

public record MemberSkillAddRequestDTO(

        @Schema(description = "기술 스택 ID 배열", example = "[1, 3, 5, ...]")
        List<Long> skillIds
) {
}
