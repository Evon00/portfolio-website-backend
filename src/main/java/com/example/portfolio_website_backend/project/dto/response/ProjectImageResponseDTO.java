package com.example.portfolio_website_backend.project.dto.response;

import com.example.portfolio_website_backend.project.domain.ProjectImage;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로젝트 이미지 응답 DTO")
public record ProjectImageResponseDTO(

        @Schema(description = "프로젝트 ID 값", example = "1")
        Long id,
        @Schema(description = "프로젝트 이미지 URL", example = "https://cloudfront/project/~~~")
        String uploadUrl,
        @Schema(description = "현재 이미지의 순서 (0부터 시작)", example = "0")
        int displayOrder
) {
    public static ProjectImageResponseDTO fromEntity(ProjectImage projectImage) {
        return new ProjectImageResponseDTO(
                projectImage.getId(),
                projectImage.getUploadUrl(),
                projectImage.getDisplayOrder()
        );
    }
}
