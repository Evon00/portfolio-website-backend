package com.example.portfolio_website_backend.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인기 기술 스택 응답 DTO")
public record PopularSkillResponseDTO(
        @Schema(description = "기술 스택 이름", example = "SpringBoot")
        String skillName,
        @Schema(description = "연결된 프로젝트 개수", example = "1")
        long count
) {
}
