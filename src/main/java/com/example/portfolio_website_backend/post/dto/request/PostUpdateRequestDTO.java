package com.example.portfolio_website_backend.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게시글 수정 요청 DTO")
public record PostUpdateRequestDTO(

        @Schema(description = "게시글 제목 (수정시에만 추가)", example = "SpringBoot 활용기")
        String title,
        @Schema(description = "게시글 요약 (수정시에만 추가)", example = "요약한 내용")
        String summary,
        @Schema(description = "게시글 내용 (수정시에만 추가)", example = "마크다운 형식의 내용")
        String content,
        @Schema(description = "읽는 시간 (수정시에만 추가)", example = "5분")
        String readTime,
        @Schema(description = "기술 스택 리스트 (수정시에만 추가)", example = "[1,2,3]")
        List<Long> skills,
        @Schema(description = "본문에 들어간 게시글 이미지 리스트 (수정시에만 추가)", example = "[2,3,4,5]")
        List<Long> images
        //이미지 업데이트가 있으면 여기에 추가
) {
}
