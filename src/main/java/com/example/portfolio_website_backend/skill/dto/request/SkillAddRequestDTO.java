package com.example.portfolio_website_backend.skill.dto.request;

import com.example.portfolio_website_backend.common.dto.ImageMetaDataDTO;
import com.example.portfolio_website_backend.skill.domain.Skill;

public record SkillAddRequestDTO(
        String skillName,
        String category

) {
    public Skill toEntity(ImageMetaDataDTO dto) {
        return Skill.builder()
                .skillName(this.skillName())
                .category(this.category())
                .originalFilename(dto.originalFilename())
                .uploadUrl(dto.uploadUrl())
                .s3Key(dto.s3Key())
                .imageWidth(dto.imageWidth())
                .imageHeight(dto.imageHeight())
                .fileSize(dto.fileSize())
                .fileExtension(dto.fileExtension())
                .build();
    }
}
