package com.example.portfolio_website_backend.post.dto.response;

import com.example.portfolio_website_backend.post.domain.Post;
import com.example.portfolio_website_backend.skill.dto.response.SkillResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Schema(description = "게시글 상세 정보 응답 DTO")
public record PostDetailResponseDTO(

        @Schema(description = "게시글 ID값", example = "1")
        Long id,
        @Schema(description = "게시글 제목", example = "SpringBoot 활용기")
        String title,
        @Schema(description = "게시글 요약", example = "게시글을 요약한 내용입니다.")
        String summary,
        @Schema(description = "게시글 내용", example = "마크다운 형식의 게시글 본문입니다.")
        String content,
        @Schema(description = "읽는 시간", example = "10분")
        String readTime,
        @Schema(description = "작성 시간", example = "2025-08-26T16:17:42.109Z")
        Instant createdAt,
        @Schema(description = "수정 시간", example = "2025-08-26T16:17:42.109Z")
        Instant modifiedAt,
        @Schema(description = "조회수", example = "0")
        Long view,
        @Schema(description = "게시글 슬러그", example = "post-springboot")
        String slug,
        @Schema(description = "기술 스택 리스트")
        List<SkillResponseDTO> skills
) {
    public static PostDetailResponseDTO create(Post post) {
        List<SkillResponseDTO> dtos = post.getPostSkills().stream()
                .sorted(Comparator.comparing(ps -> ps.getSkill().getId()))
                .map(ps -> SkillResponseDTO.fromEntity(ps.getSkill()))
                .toList();

        return new PostDetailResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getSummary(),
                post.getContent(),
                post.getReadTime(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                post.getView(),
                post.getSlug(),
                dtos
        );
    }
}
