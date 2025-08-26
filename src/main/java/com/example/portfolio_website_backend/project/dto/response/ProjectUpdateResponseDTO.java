package com.example.portfolio_website_backend.project.dto.response;

import com.example.portfolio_website_backend.project.domain.Project;
import com.example.portfolio_website_backend.skill.dto.response.SkillResponseDTO;

import java.time.Instant;
import java.util.List;

public record ProjectUpdateResponseDTO(
        String title,
        String summary,
        String githubUrl,
        String demoUrl,
        Instant startDate,
        Instant endDate,
        List<SkillResponseDTO> skills,
        int imageCount,
        String slug
) {
    public static ProjectUpdateResponseDTO fromEntity(Project project, List<SkillResponseDTO> skills, int count) {
        return new ProjectUpdateResponseDTO(
                project.getTitle(),
                project.getSummary(),
                project.getGithubUrl(),
                project.getDemoUrl(),
                project.getStartDate(),
                project.getEndDate(),
                skills,
                count,
                project.getSlug());
    }
}
