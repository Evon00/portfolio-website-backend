package com.example.portfolio_website_backend.stats.dto.response;

import java.util.List;

public record PostStatsResponseDTO(
        List<PostDataResponseDTO> posts
) {
}
