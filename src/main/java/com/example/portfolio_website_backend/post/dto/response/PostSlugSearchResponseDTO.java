package com.example.portfolio_website_backend.post.dto.response;

import java.util.List;

public record PostSlugSearchResponseDTO(
        List<String> slugs
) {
}
