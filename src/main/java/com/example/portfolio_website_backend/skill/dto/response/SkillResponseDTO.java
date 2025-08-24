package com.example.portfolio_website_backend.skill.dto.response;

import com.example.portfolio_website_backend.skill.domain.Skill;

import java.time.Instant;

public record SkillResponseDTO(
        Long id,
        String skillName,
        String category,
        String uploadUrl,
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
