package com.example.portfolio_website_backend.skill.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "기술 스택 조회 응답 DTO")
public record SkillListResponseDTO(

        @Schema(description = "기술 스택 조회 목록")
        List<SkillResponseDTO> skills
) {
}
