package com.example.portfolio_website_backend.skill.dto.response;

import java.util.List;

public record SkillListResponseDTO(
        List<SkillResponseDTO> skills
) {
}
