package com.example.portfolio_website_backend.project.domain;

import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.project.dto.request.ProjectUpdateRequestDTO;
import com.example.portfolio_website_backend.skill.domain.Skill;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProjectSkill> projectSkills = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String summary;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(name = "github_url")
    private String githubUrl;
    @Column(name = "demo_url")
    private String demoUrl;
    @Column(name = "start_date", nullable = false)
    private Instant startDate;
    @Column(name = "end_date")
    private Instant endDate;
    @Column(nullable = false, unique = true)
    private String slug;
    @Column(nullable = false)
    private boolean isFeatured;

    @Builder
    public Project(Member member, String title, String summary, String content, String githubUrl, String demoUrl, Instant startDate, Instant endDate, String slug) {
        this.member = member;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.githubUrl = githubUrl;
        this.demoUrl = demoUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.slug = slug;
        this.isFeatured = false;
    }

    public void addProjectSkill(Skill skill) {
        ProjectSkill projectSkill = ProjectSkill.builder()
                .project(this)
                .skill(skill)
                .build();
        this.projectSkills.add(projectSkill);
    }

    public void update(ProjectUpdateRequestDTO requestDTO) {
        if (requestDTO.title() != null) this.title = requestDTO.title();
        if (requestDTO.summary() != null) this.summary = requestDTO.summary();
        if (requestDTO.content() != null) this.content = requestDTO.content();
        if (requestDTO.githubUrl() != null) this.githubUrl = requestDTO.githubUrl();
        if (requestDTO.demoUrl() != null) this.demoUrl = requestDTO.demoUrl();
        if (requestDTO.startDate() != null) this.startDate = requestDTO.startDate();
        if (requestDTO.endDate() != null) this.endDate = requestDTO.endDate();
    }

    public void setFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }
}
