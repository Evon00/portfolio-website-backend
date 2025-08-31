package com.example.portfolio_website_backend.stats.dto.response;

import com.example.portfolio_website_backend.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 데이터 DTO")
public record PostDataResponseDTO(
        @Schema(description = "게시글 슬러그", example = "post-springboot")
        String slug,
        @Schema(description = "게시글 제목", example = "SpringBoot 활용기")
        String title,
        @Schema(description = "조회수", example = "3")
        Long view
) {
    public static PostDataResponseDTO fromEntity(Post post){
        return new PostDataResponseDTO(
                post.getSlug(),
                post.getTitle(),
                post.getView()
        );
    }
}
