package com.example.portfolio_website_backend.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "프로젝트 슬러그 검색 응답 DTO")
public record ProjectSlugSearchResponseDTO(
        @Schema(description = "프로젝트 슬러그 리스트")
        List<String> slugs
) {
}
