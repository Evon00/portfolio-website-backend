package com.example.portfolio_website_backend.skill.dto.response;

import com.example.portfolio_website_backend.skill.domain.Skill;
import org.springframework.data.domain.Page;

import java.util.List;

public record SkillPageResponseDTO(
        List<SkillResponseDTO> skills,
        int totalPages,
        Long totalElements,
        int size,
        int number,
        boolean first,
        boolean last

) {

    public static SkillPageResponseDTO createSkillPageResponseDTO(List<SkillResponseDTO> skillList, Page<Skill> page) {
        return new SkillPageResponseDTO(
                skillList,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.isFirst(),
                page.isLast()
        );
    }
}
