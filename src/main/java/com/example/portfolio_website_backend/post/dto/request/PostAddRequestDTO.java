package com.example.portfolio_website_backend.post.dto.request;

import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게시글 추가 요청 DTO")
public record PostAddRequestDTO(

        @Schema(description = "게시글 제목", example = "SpringBoot 입문기")
        String title,
        @Schema(description = "게시글 요약", example = "SpringBoot를 활용한 글입니다.")
        String summary,
        @Schema(description = "게시글 내용", example = "마크다운 형식의 게시글 내용이 들어갑니다.")
        String content,
        @Schema(description = "읽는 시간", example = "10분")
        String readTime,
        @Schema(description = "게시글 슬러그", example = "post-springboot")
        String slug,
        @Schema(description = "기술 스택 리스트", example = "[1, 5, 6]")
        List<Long> skills,
        @Schema(description = "본문에 들어간 게시글 이미지 리스트", example = "[1,2,3]")
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
