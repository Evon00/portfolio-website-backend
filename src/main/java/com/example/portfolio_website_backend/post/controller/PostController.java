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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final ViewService viewService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/image")
    public ResponseEntity<SuccessResponse<PostImageResponseDTO>> uploadPostImage(@RequestPart("data") String slug, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.uploadPostImage(file, slug)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SuccessResponse<PostResponseDTO>> addPost(@RequestBody PostAddRequestDTO requestDTO, @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.addPost(requestDTO, member)));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<PostPageResponseDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.getAllPosts(page, size, sortBy, sortDir)));
    }

    @GetMapping(value = "/slug/{slug}")
    public ResponseEntity<SuccessResponse<PostDetailResponseDTO>> getPostDetail(
            @PathVariable String slug,
            HttpServletRequest request
    ) {
        PostDetailResponseDTO response = postService.getPostDetail(slug);
        if (response != null) {
            viewService.addView(response.id(), request);
        }
        return ResponseEntity.ok(SuccessResponse.ok(postService.getPostDetail(slug)));
    }

    @GetMapping(value = "/slug/search")
    public ResponseEntity<SuccessResponse<PostSlugSearchResponseDTO>> searchPostSlug(@RequestParam String keyword) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.searchPostSlug(keyword)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<PostResponseDTO>> updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDTO requestDTO,
            @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(postService.updatePost(id, requestDTO, member)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<PostDeleteResponseDTO>> deletePost(
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
