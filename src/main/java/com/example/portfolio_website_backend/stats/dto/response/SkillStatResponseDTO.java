package com.example.portfolio_website_backend.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "기술 스택 통계 응답 DTO")
public record SkillStatResponseDTO(
        @Schema(description = "기술 스택 통계 리스트")
        List<PopularSkillResponseDTO> skills
) {
}
