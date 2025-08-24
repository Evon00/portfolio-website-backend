package com.example.portfolio_website_backend.skill.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기술 스택 수정 요청 DTO")
public record SkillUpdateRequestDTO(
        @Schema(description = "기술 스택 이름", example = "SpringBoot")
        String skillName,
        @Schema(description = "기술 스택 카테고리 이름", example = "Backend")
        String category
) {
}
