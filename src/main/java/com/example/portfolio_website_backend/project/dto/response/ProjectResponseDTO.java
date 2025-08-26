package com.example.portfolio_website_backend.project.dto.response;

import com.example.portfolio_website_backend.project.domain.Project;
import com.example.portfolio_website_backend.project.domain.ProjectImage;
import com.example.portfolio_website_backend.project.domain.ProjectSkill;
import com.example.portfolio_website_backend.skill.dto.response.SkillResponseDTO;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public record ProjectResponseDTO(
        Long id,
        String title,
        String summary,
        String content,
        String githubUrl,
        String demoUrl,
        Instant startDate,
        Instant endDate,
        String slug,
        boolean isFeatured,
        List<SkillResponseDTO> skills,
        List<ProjectImageResponseDTO> images
) {
    public static ProjectResponseDTO fromEntity(Project project, List<ProjectSkill> projectSkills, List<ProjectImage> projectImages) {

        List<SkillResponseDTO> skillResponseDTOS = projectSkills
                .stream()
                .sorted(Comparator.comparing(ps -> ps.getSkill().getId()))
                .map(ps -> SkillResponseDTO.fromEntity(ps.getSkill()))
                .toList();

        List<ProjectImageResponseDTO> projectImageResponseDTOS = projectImages
                .stream()
                .map(ProjectImageResponseDTO::fromEntity)
                .toList();

        return new ProjectResponseDTO(
                project.getId(),
                project.getTitle(),
                project.getSummary(),
                project.getContent(),
                project.getGithubUrl(),
                project.getDemoUrl(),
                project.getStartDate(),
                project.getEndDate(),
                project.getSlug(),
                project.isFeatured(),
                skillResponseDTOS,
                projectImageResponseDTOS
        );
    }
}
