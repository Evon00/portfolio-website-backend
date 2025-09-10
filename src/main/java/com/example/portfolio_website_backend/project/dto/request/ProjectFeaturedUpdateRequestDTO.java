package com.example.portfolio_website_backend.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "주요 프로젝트 선정 요청 DTO")
public record ProjectFeaturedUpdateRequestDTO(

        @Schema(description = "선정할 프로젝트 ID 리스트", example = "[1,2,3]")
        List<Long> projects
) {
}
