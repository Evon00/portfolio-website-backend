package com.example.portfolio_website_backend.project.dto.response;

import com.example.portfolio_website_backend.project.domain.Project;
import com.example.portfolio_website_backend.project.domain.ProjectImage;
import com.example.portfolio_website_backend.project.domain.ProjectSkill;
import com.example.portfolio_website_backend.skill.dto.response.SkillResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Schema(description = "프로젝트 응답 DTO")
public record ProjectResponseDTO(
        @Schema(description = "프로젝트 ID 값", example = "1")
        Long id,
        @Schema(description = "프로젝트 제목", example = "AI 활용 웹 사이트 개발")
        String title,
        @Schema(description = "프로젝트 요약", example = "claude를 활용한 간단히 만드는 웹사이트 입니다.")
        String summary,
        @Schema(description = "프로젝트 내용", example = "프로젝트에 대한 설명, 마크다운 형식의 글이 적힐 예정입니다.")
        String content,
        @Schema(description = "깃헙 주소", example = "https://github/~~")
        String githubUrl,
        @Schema(description = "사이트 주소", example = "https://demo/~~")
        String demoUrl,
        @Schema(description = "프로젝트 시작 날짜", example = "2025-08-26T16:17:42.109Z")
        Instant startDate,
        @Schema(description = "프로젝트 종료 날짜", example = "2025-08-27T16:17:42.109Z")
        Instant endDate,
        @Schema(description = "프로젝트 슬러그", example = "ai-project")
        String slug,
        @Schema(description = "주요 프로젝트 선정 여부", examples = {"false","true"})
        boolean isFeatured,
        @Schema(description = "프로젝트 기술 스택 리스트")
        List<SkillResponseDTO> skills,
        @Schema(description = "프로젝트 이미지 리스트")
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
