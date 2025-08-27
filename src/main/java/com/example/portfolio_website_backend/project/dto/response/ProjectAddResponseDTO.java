package com.example.portfolio_website_backend.project.dto.response;

import com.example.portfolio_website_backend.project.domain.Project;
import com.example.portfolio_website_backend.skill.dto.response.SkillResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "프로젝트 추가 후 응답 DTO")
public record ProjectAddResponseDTO(
        @Schema(description = "프로젝트 제목", example = "AI 활용 웹 사이트 개발")
        String title,
        @Schema(description = "프로젝트 요약", example = "claude를 활용한 간단히 만드는 웹사이트 입니다.")
        String summary,
        @Schema(description = "깃헙 주소", example = "https://github/~~")
        String githubUrl,
        @Schema(description = "사이트 주소", example = "https://demo/~~")
        String demoUrl,
        @Schema(description = "프로젝트 시작 날짜", example = "2025-08-26T16:17:42.109Z")
        Instant startDate,
        @Schema(description = "프로젝트 종료 날짜", example = "2025-08-27T16:17:42.109Z")
        Instant endDate,
        @Schema(description = "프로젝트 기술 스택 응답 DTO")
        List<SkillResponseDTO> skills,
        @Schema(description = "프로젝트 현재 이미지 개수", example = "3")
        int imageCount,
        @Schema(description = "프로젝트 슬러그", example = "ai-project")
        String slug
) {
    public static ProjectAddResponseDTO fromEntity(Project project, List<SkillResponseDTO> skills, int count) {
        return new ProjectAddResponseDTO(
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
