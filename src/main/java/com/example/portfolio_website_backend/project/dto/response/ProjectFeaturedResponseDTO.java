package com.example.portfolio_website_backend.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "주요 프로젝트 선정 응답 DTO")
public record ProjectFeaturedResponseDTO(
        @Schema(description = "주요 프로젝트 리스트")
        List<ProjectResponseDTO> projects
) {
}
