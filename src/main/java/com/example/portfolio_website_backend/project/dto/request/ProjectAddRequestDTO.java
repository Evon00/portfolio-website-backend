package com.example.portfolio_website_backend.project.dto.request;

import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.project.domain.Project;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "프로젝트 추가 요청 DTO")
public record ProjectAddRequestDTO(

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
        @Schema(description = "기술 스택 ID 값 리스트", example = "[1, 5, 7, 10]")
        List<Long> skillIds,
        @Schema(description = "프로젝트 이미지 표기 순서 (이미지 파일 포함시)", example = "[0, 2, 1]")
        List<Integer> displayOrder,
        @Schema(description = "프로젝트 슬러그", example = "ai-project")
        String slug
) {
    public Project toEntity(Member member) {
        return Project.builder()
                .member(member)
                .title(this.title)
                .summary(this.summary)
                .content(this.content)
                .githubUrl(this.githubUrl)
                .demoUrl(this.demoUrl)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .slug(this.slug)
                .build();
    }
}
