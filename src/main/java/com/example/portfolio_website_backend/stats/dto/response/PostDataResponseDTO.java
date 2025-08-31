package com.example.portfolio_website_backend.stats.dto.response;

import com.example.portfolio_website_backend.post.domain.Post;

public record PostDataResponseDTO(
        String slug,
        String title,
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
