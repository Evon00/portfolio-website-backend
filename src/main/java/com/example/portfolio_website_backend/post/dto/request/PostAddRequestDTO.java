package com.example.portfolio_website_backend.post.dto.request;

import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.post.domain.Post;

import java.util.List;

public record PostAddRequestDTO(
        String title,
        String summary,
        String content,
        String readTime,
        String slug,
        List<Long> skills,
        List<Long> images
) {
    public Post toEntity(Member member) {
        return Post.builder()
                .member(member)
                .title(this.title)
                .summary(this.summary)
                .content(this.content)
                .readTime(this.readTime)
                .slug(this.slug)
                .build();
    }
}
