package com.example.portfolio_website_backend.project.dto.response;

import com.example.portfolio_website_backend.project.domain.ProjectImage;

public record ProjectImageResponseDTO(
        Long id,
        String uploadUrl,
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
