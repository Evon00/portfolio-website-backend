package com.example.portfolio_website_backend.skill.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "기술 스택의 카테고리 반환 DTO")
public record SkillCategoryResponseDTO(

        @Schema(description = "기술 스택 카테고리 목록", example = "{'Backend','Frontend','DB', ...}")
        List<String> categories
) {
}
