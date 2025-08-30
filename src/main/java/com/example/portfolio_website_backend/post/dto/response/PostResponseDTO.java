package com.example.portfolio_website_backend.post.dto.response;

import com.example.portfolio_website_backend.post.domain.Post;
import com.example.portfolio_website_backend.skill.dto.response.SkillResponseDTO;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public record PostResponseDTO(
        Long id,
        String title,
        String summary,
        String readTime,
        Instant createdAt,
        Long view,
        String slug,
        List<SkillResponseDTO> skills

) {
    public static PostResponseDTO create(Post post) {
        List<SkillResponseDTO> dtos = post.getPostSkills().stream()
                .sorted(Comparator.comparing(ps -> ps.getSkill().getId()))
                .map(ps -> SkillResponseDTO.fromEntity(ps.getSkill()))
                .toList();

        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getSummary(),
                post.getReadTime(),
                post.getCreatedAt(),
                post.getView(),
                post.getSlug(),
                dtos
        );
    }
}
