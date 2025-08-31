package com.example.portfolio_website_backend.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "요약 통계 응답 DTO")
public record SummaryStatsResponseDTO(
        @Schema(description = "총 게시글 개수", example = "10")
        long totalPosts,
        @Schema(description = "총 프로젝트 개수", example = "4")
        long totalProjects,
        @Schema(description = "총 조회수", example = "340")
        long totalViews,
        @Schema(description = "평균 조회수", example = "34")
        double avgViews
) {
}
