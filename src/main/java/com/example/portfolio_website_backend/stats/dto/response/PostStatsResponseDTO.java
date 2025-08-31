package com.example.portfolio_website_backend.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게시글 통계 응답 DTO")
public record PostStatsResponseDTO(
        @Schema(description = "게시글 통계 리스트")
        List<PostDataResponseDTO> posts
) {
}
