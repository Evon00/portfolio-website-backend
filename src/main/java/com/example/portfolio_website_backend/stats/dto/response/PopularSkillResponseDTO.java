package com.example.portfolio_website_backend.stats.dto.response;

public record PopularSkillResponseDTO(
        String skillName,
        long count
) {
}
