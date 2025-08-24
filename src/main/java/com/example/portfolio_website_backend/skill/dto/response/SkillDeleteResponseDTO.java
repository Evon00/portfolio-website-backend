package com.example.portfolio_website_backend.skill.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기술 스택 삭제 반환 DTO")
public record SkillDeleteResponseDTO(

        @Schema(description = "삭제된 기술 스택 ID값", example = "1")
        Long id
) {
}
