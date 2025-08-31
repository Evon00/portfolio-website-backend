package com.example.portfolio_website_backend.stats.controller;

import com.example.portfolio_website_backend.common.dto.SuccessResponse;
import com.example.portfolio_website_backend.stats.dto.response.PostStatsResponseDTO;
import com.example.portfolio_website_backend.stats.dto.response.SkillStatResponseDTO;
import com.example.portfolio_website_backend.stats.dto.response.SummaryStatsResponseDTO;
import com.example.portfolio_website_backend.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/summary")
    public ResponseEntity<SuccessResponse<SummaryStatsResponseDTO>> getSummaryStats(){
        return ResponseEntity.ok(SuccessResponse.ok(statsService.getSummaryStats()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/posts/recent")
    public ResponseEntity<SuccessResponse<PostStatsResponseDTO>> getRecentPosts(){
        return ResponseEntity.ok(SuccessResponse.ok(statsService.getRecentPosts()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/posts/popular")
    public ResponseEntity<SuccessResponse<PostStatsResponseDTO>> getPopularPosts(){
        return ResponseEntity.ok(SuccessResponse.ok(statsService.getPopularPosts()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/skills/popular")
    public ResponseEntity<SuccessResponse<SkillStatResponseDTO>> getPopularSkills(){
        return ResponseEntity.ok(SuccessResponse.ok(statsService.getPopularSkills()));
    }
}
