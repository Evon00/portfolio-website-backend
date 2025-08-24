package com.example.portfolio_website_backend.project.domain;

import com.example.portfolio_website_backend.skill.domain.Skill;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project_skill", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "skill_id"}))
public class ProjectSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Builder
    public ProjectSkill(Project project, Skill skill) {
        this.project = project;
        this.skill = skill;
    }

    protected void setProject(Project project) {
        this.project = project;
    }

    protected void setSkill(Skill skill) {
        this.skill = skill;
    }
}
