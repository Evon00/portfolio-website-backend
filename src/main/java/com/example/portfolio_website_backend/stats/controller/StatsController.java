package com.example.portfolio_website_backend.stats.controller;

import com.example.portfolio_website_backend.common.dto.SuccessResponse;
import com.example.portfolio_website_backend.stats.dto.response.PostStatsResponseDTO;
import com.example.portfolio_website_backend.stats.dto.response.SkillStatResponseDTO;
import com.example.portfolio_website_backend.stats.dto.response.SummaryStatsResponseDTO;
import com.example.portfolio_website_backend.stats.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Stats API", description = "통계 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "요약 통계 조회", description = "총 게시글 수, 총 프로젝트 수, 총 조회수, 평균 조회수를 반환합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/summary")
    public ResponseEntity<SuccessResponse<SummaryStatsResponseDTO>> getSummaryStats(){
        return ResponseEntity.ok(SuccessResponse.ok(statsService.getSummaryStats()));
    }

    @Operation(summary = "최신 게시글 조회", description = "통계용 최신 5개의 게시글을 조회합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/posts/recent")
    public ResponseEntity<SuccessResponse<PostStatsResponseDTO>> getRecentPosts(){
        return ResponseEntity.ok(SuccessResponse.ok(statsService.getRecentPosts()));
    }


    @Operation(summary = "인기 게시글 조회", description = "통계용 인기순으로 5개의 게시글을 조회합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/posts/popular")
    public ResponseEntity<SuccessResponse<PostStatsResponseDTO>> getPopularPosts(){
        return ResponseEntity.ok(SuccessResponse.ok(statsService.getPopularPosts()));
    }

    @Operation(summary = "인기 기술 스택 조회", description = "통계용 프로젝트에 사용된 기술스택을 인기순으로 5개를 조회합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/skills/popular")
    public ResponseEntity<SuccessResponse<SkillStatResponseDTO>> getPopularSkills(){
        return ResponseEntity.ok(SuccessResponse.ok(statsService.getPopularSkills()));
    }
}
