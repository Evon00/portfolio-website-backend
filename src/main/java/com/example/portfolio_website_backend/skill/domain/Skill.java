package com.example.portfolio_website_backend.skill.domain;

import com.example.portfolio_website_backend.member.domain.MemberSkill;
import com.example.portfolio_website_backend.post.domain.PostSkill;
import com.example.portfolio_website_backend.project.domain.ProjectSkill;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(nullable = false)
    private String category;

    @Column(name = "icon_url")
    private String iconUrl;

    @OneToMany(mappedBy = "skill",cascade = CascadeType.ALL)
    private List<ProjectSkill> skillProjects = new ArrayList<>();

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<PostSkill> skillPosts = new ArrayList<>();

    @OneToMany(mappedBy = "skill",cascade = CascadeType.ALL)
    private List<MemberSkill> skillMembers = new ArrayList<>();

    @Builder
    public Skill(String skillName, String category, String iconUrl){
        this.skillName = skillName;
        this.category = category;
        this.iconUrl = iconUrl;
    }
}
