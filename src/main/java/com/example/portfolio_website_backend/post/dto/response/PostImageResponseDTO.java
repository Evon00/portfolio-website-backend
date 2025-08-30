package com.example.portfolio_website_backend.post.dto.response;

import com.example.portfolio_website_backend.post.domain.PostImage;

public record PostImageResponseDTO(
        Long id,
        String uploadUrl
) {
    public static PostImageResponseDTO create(PostImage postImage) {
        return new PostImageResponseDTO(
                postImage.getId(),
                postImage.getUploadUrl()
        );
    }
}
