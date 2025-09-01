package com.example.portfolio_website_backend.post.dto.response;

import com.example.portfolio_website_backend.post.domain.Post;
import com.example.portfolio_website_backend.post.domain.PostImage;
import com.example.portfolio_website_backend.skill.dto.response.SkillResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Schema(description = "게시글 요약 응답 DTO")
public record PostResponseDTO(
        @Schema(description = "게시글 ID값", example = "1")
        Long id,
        @Schema(description = "게시글 제목", example = "SpringBoot 활용기")
        String title,
        @Schema(description = "게시글 요약", example = "본문을 요약한 내용입니다.")
        String summary,
        @Schema(description = "읽는 시간", example = "10분")
        String readTime,
        @Schema(description = "작성 시간", example = "2025-08-26T16:17:42.109Z")
        Instant createdAt,
        @Schema(description = "조회수", example = "0")
        Long view,
        @Schema(description = "게시글 슬러그", example = "post-springboot")
        String slug,
        @Schema(description = "게시글 기술 스택 리스트")
        List<SkillResponseDTO> skills,

        @Schema(description = "게시글 이미지 리스트")
        List<PostImageResponseDTO> images

) {
    public static PostResponseDTO create(Post post, Map<Long, List<PostImage>> imagesByPostId) {
        List<SkillResponseDTO> skills = post.getPostSkills().stream()
                .sorted(Comparator.comparing(ps -> ps.getSkill().getId()))
                .map(ps -> SkillResponseDTO.fromEntity(ps.getSkill()))
                .toList();

        List<PostImage> imageResponseDTOS = imagesByPostId.getOrDefault(post.getId(), Collections.emptyList());

        List<PostImageResponseDTO> images = imageResponseDTOS.stream()
                .map(PostImageResponseDTO::create)
                .toList();

        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getSummary(),
                post.getReadTime(),
                post.getCreatedAt(),
                post.getView(),
                post.getSlug(),
                skills,
                images
        );
    }

    public static PostResponseDTO create(Post post, List<PostImage> postImages) {
        List<SkillResponseDTO> skills = post.getPostSkills().stream()
                .sorted(Comparator.comparing(ps -> ps.getSkill().getId()))
                .map(ps -> SkillResponseDTO.fromEntity(ps.getSkill()))
                .toList();

        List<PostImageResponseDTO> images = postImages.stream()
                .map(PostImageResponseDTO::create)
                .toList();

        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getSummary(),
                post.getReadTime(),
                post.getCreatedAt(),
                post.getView(),
                post.getSlug(),
                skills,
                images
        );
    }
}
