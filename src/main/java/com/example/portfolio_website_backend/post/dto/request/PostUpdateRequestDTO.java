package com.example.portfolio_website_backend.post.dto.request;

import java.util.List;

public record PostUpdateRequestDTO(
        String title,
        String summary,
        String content,
        String readTime,
        List<Long> skills,
        List<Long> images
        //이미지 업데이트가 있으면 여기에 추가
) {
}
