package com.example.portfolio_website_backend.stats.dto.response;

public record SummaryStatsResponseDTO(
        long totalPosts,
        long totalProjects,
        long totalViews,
        double avgViews
) {
}
