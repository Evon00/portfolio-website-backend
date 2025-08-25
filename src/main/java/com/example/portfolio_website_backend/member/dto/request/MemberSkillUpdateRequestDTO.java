package com.example.portfolio_website_backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "회원의 기술 스택 수정 요청 DTO")
public record MemberSkillUpdateRequestDTO(

        @Schema(description = "수정된 기술 스택 ID 배열", example = "[3, 5, 6, 11, ...]")
        List<Long> skillIds
) {
}
