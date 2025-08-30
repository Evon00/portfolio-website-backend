package com.example.portfolio_website_backend.post.dto.response;

import com.example.portfolio_website_backend.post.domain.Post;

public record DailyViewStats(
        Post post,
        Long totalViews,
        Long uniqueVisitors
) {
}
