package com.example.portfolio_website_backend.member.dto.request;

public record MemberUpdateRequestDTO(
        String name,
        String description,
        String githubUrl,
        String emailUrl,
        String profileUrl,
        String s3Key
) {
}
