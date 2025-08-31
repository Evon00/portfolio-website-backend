package com.example.portfolio_website_backend.stats.service;

import com.example.portfolio_website_backend.post.domain.Post;
import com.example.portfolio_website_backend.post.repository.PostRepository;
import com.example.portfolio_website_backend.project.repository.ProjectRepository;
import com.example.portfolio_website_backend.project.repository.ProjectSkillRepository;
import com.example.portfolio_website_backend.stats.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final PostRepository postRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;

    public SummaryStatsResponseDTO getSummaryStats(){
        long totalPosts = postRepository.count();
        long totalProjects = projectRepository.count();
        long totalViews = postRepository.getTotalViews();
        double avgViews = totalPosts > 0 ? (double) totalViews / totalPosts : 0;

        return new SummaryStatsResponseDTO(
                totalPosts,
                totalProjects,
                totalViews,
                avgViews);
    }

    public PostStatsResponseDTO getRecentPosts() {
        List<Post> posts = postRepository.findTop5ByOrderByCreatedAtDesc();
        List<PostDataResponseDTO> dtos = new ArrayList<>();
        if(!posts.isEmpty()){
            dtos = posts.stream()
                    .map(PostDataResponseDTO::fromEntity)
                    .toList();
        }

        return new PostStatsResponseDTO(dtos);
    }

    public PostStatsResponseDTO getPopularPosts() {
        List<Post> posts = postRepository.findTop5ByOrderByViewDesc();
        List<PostDataResponseDTO> dtos = new ArrayList<>();
        if(!posts.isEmpty()){
            dtos = posts.stream()
                    .map(PostDataResponseDTO::fromEntity)
                    .toList();
        }

        return new PostStatsResponseDTO(dtos);
    }

    public SkillStatResponseDTO getPopularSkills() {
        List<PopularSkillResponseDTO> skills = projectSkillRepository.findPopularSkills(PageRequest.of(0, 5));

        if(skills.isEmpty())
            return new SkillStatResponseDTO(Collections.emptyList());
        else
            return new SkillStatResponseDTO(skills);
    }
}
