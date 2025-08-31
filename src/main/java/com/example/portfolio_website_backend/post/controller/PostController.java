package com.example.portfolio_website_backend.post.controller;

import com.example.portfolio_website_backend.common.dto.SuccessResponse;
import com.example.portfolio_website_backend.common.security.annotation.CurrentMember;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.post.dto.request.PostAddRequestDTO;
import com.example.portfolio_website_backend.post.dto.request.PostUpdateRequestDTO;
import com.example.portfolio_website_backend.post.dto.response.*;
import com.example.portfolio_website_backend.post.service.PostService;
import com.example.portfolio_website_backend.post.service.ViewService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Post API", description = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final ViewService viewService;

    @Operation(summary = "게시글 이미지 업로드", description = "게시글 본문에 들어갈 이미지를 업로드합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<PostImageResponseDTO>> uploadPostImage(
            @Parameter(description = "게시글 슬러그")
            @RequestPart("data") String slug,
            @Parameter(description = "게시글 이미지 파일 (jpg, png, gif, svg 지원, 최대 5MB)")
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.uploadPostImage(file, slug)));
    }

    @Operation(summary = "게시글 추가", description = "게시글 정보를 통해 게시글을 추가합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SuccessResponse<PostResponseDTO>> addPost(
            @Parameter(description = "추가할 게시글 정보")
            @RequestBody PostAddRequestDTO requestDTO,
            @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.addPost(requestDTO, member)));
    }

    @Operation(summary = "게시글 조회", description = "게시글을 페이지 단위로 조회합니다.")
    @Parameters({
            @Parameter(name = "page", description = "현재 page 값, default = 0"),
            @Parameter(name = "size", description = "한 페이지의 데이터 크기, default = 12"),
            @Parameter(name = "sortBy", description = "정렬 기준, default = createdAt (createdAt || view)"),
            @Parameter(name = "sortDir", description = "정렬 순서 , default = desc (desc || asc)")
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<PostPageResponseDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.getAllPosts(page, size, sortBy, sortDir)));
    }

    @Operation(summary = "게시글 조회 - 슬러그", description = "슬러그를 통해 게시글을 조회하고, 조회수가 증가됩니다.")
    @GetMapping(value = "/slug/{slug}")
    public ResponseEntity<SuccessResponse<PostDetailResponseDTO>> getPostDetail(
            @Parameter(description = "게시글 슬러그")
            @PathVariable String slug,
            HttpServletRequest request
    ) {
        PostDetailResponseDTO response = postService.getPostDetail(slug);
        if (response != null) {
            viewService.addView(response.id(), request);
        }
        return ResponseEntity.ok(SuccessResponse.ok(postService.getPostDetail(slug)));
    }

    @Operation(summary = "게시글 슬러그 조회", description = "해당 되는 슬러그로 시작하는 게시글 슬러그를 반환합니다.")
    @GetMapping(value = "/slug/search")
    public ResponseEntity<SuccessResponse<PostSlugSearchResponseDTO>> searchPostSlug(
            @Parameter(description = "검색할 키워드")
            @RequestParam String keyword) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.searchPostSlug(keyword)));
    }

    @Operation(summary = "게시글 수정", description = "수정할 데이터를 통해 게시글을 수정합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<PostResponseDTO>> updatePost(
            @Parameter(description = "게시글 ID값")
            @PathVariable Long id,
            @Parameter(description = "수정할 게시글 정보 DTO")
            @RequestBody PostUpdateRequestDTO requestDTO,
            @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.updatePost(id, requestDTO, member)));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 ID값을 통해 게시글을 삭제합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<PostDeleteResponseDTO>> deletePost(
            @Parameter(description = "게시글 ID값")
            @PathVariable Long id,
            @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.deletePost(id, member)));
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/view/test")
    public ResponseEntity<SuccessResponse> viewTest() {
        viewService.aggregateDailyViews();
        return ResponseEntity.ok(SuccessResponse.ok(null));
    }

}
