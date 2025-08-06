package com.example.portfolio_website_backend.project.domain;

import com.example.portfolio_website_backend.member.domain.Member;
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

    @Column(nullable = false)
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

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectSkill> projectSkills = new ArrayList<>();

    @Builder
    public Project(Member member, String title, String summary, String content, String githubUrl, String demoUrl, Instant startDate, Instant endDate, String slug){
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

    public void addProjectSkill(Skill skill){
        ProjectSkill projectSkill = ProjectSkill.builder()
                .project(this)
                .skill(skill)
                .build();
        this.projectSkills.add(projectSkill);
    }

    public void removeProjectSkill(ProjectSkill projectSkill) {
        this.projectSkills.remove(projectSkill);
    }
}
