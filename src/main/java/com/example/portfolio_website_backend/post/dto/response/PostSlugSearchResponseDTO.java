package com.example.portfolio_website_backend.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게시글 슬러그 검색 응답 DTO")
public record PostSlugSearchResponseDTO(

        @Schema(description = "게시글 슬러그 리스트", example = "['post-springboot', 'post-nodejs']")
        List<String> slugs
) {
}
