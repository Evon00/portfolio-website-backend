package com.example.portfolio_website_backend.post.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 삭제 응답 DTO")
public record PostDeleteResponseDTO(
        @Schema(description = "삭제된 게시글 ID값", example = "1")
        Long id
) {
}
