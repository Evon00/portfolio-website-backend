package com.example.portfolio_website_backend.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로젝트 삭제 응답 DTO")
public record ProjectDeleteResponseDTO(
        @Schema(description = "삭제된 프로젝트 ID 값", example = "1")
        Long id
) {
}
