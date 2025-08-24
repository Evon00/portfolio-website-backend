package com.example.portfolio_website_backend.skill.dto.response;

import com.example.portfolio_website_backend.skill.domain.Skill;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "기술 스택 반환 DTO")
public record SkillResponseDTO(

        @Schema(description = "기술 스택 ID값", example = "1")
        Long id,
        @Schema(description = "기술 스택 이름", example = "SpringBoot")
        String skillName,

        @Schema(description = "기술 스택 카테고리 이름", example = "Backend")
        String category,

        @Schema(description = "기술 스택 아이콘 URL", example = "https://~~~~/icon/~~~.svg")
        String uploadUrl,

        @Schema(description = "기술 스택 생성 시간", example = "2025-08-22T08:08:31.387258Z")
        Instant createdAt
) {
    public static SkillResponseDTO fromEntity(Skill skill) {
        return new SkillResponseDTO(
                skill.getId(),
                skill.getSkillName(),
                skill.getCategory(),
                skill.getUploadUrl(),
                skill.getCreatedAt()
        );
    }
}
