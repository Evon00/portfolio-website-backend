package com.example.portfolio_website_backend.skill.dto.request;

import com.example.portfolio_website_backend.common.dto.ImageMetaDataDTO;
import com.example.portfolio_website_backend.skill.domain.Skill;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기술 스택 추가 요청 DTO")
public record SkillAddRequestDTO(
        @Schema(description = "기술 스택 이름", example = "SpringBoot")
        String skillName,
        @Schema(description = "기술 스택 카테고리 이름", example = "Backend")
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
