package com.example.portfolio_website_backend.post.dto.response;

import com.example.portfolio_website_backend.post.domain.PostImage;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 이미지 응답 DTO")
public record PostImageResponseDTO(
        @Schema(description = "이미지 ID값", example = "1")
        Long id,
        @Schema(description = "업로드된 이미지 URL", example = "cloudfront/post/~~.png")
        String uploadUrl
) {
    public static PostImageResponseDTO create(PostImage postImage) {
        return new PostImageResponseDTO(
                postImage.getId(),
                postImage.getUploadUrl()
        );
    }
}
