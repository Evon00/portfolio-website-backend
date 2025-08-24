package com.example.portfolio_website_backend.skill.domain;

import com.example.portfolio_website_backend.common.domain.ImageContent;
import com.example.portfolio_website_backend.common.dto.ImageMetaDataDTO;
import com.example.portfolio_website_backend.member.domain.MemberSkill;
import com.example.portfolio_website_backend.post.domain.PostSkill;
import com.example.portfolio_website_backend.project.domain.ProjectSkill;
import com.example.portfolio_website_backend.skill.dto.request.SkillUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
public class Skill extends ImageContent {

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(nullable = false)
    private String category;

    @Builder.Default
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<ProjectSkill> skillProjects = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<PostSkill> skillPosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<MemberSkill> skillMembers = new ArrayList<>();

    public void update(SkillUpdateRequestDTO requestDTO) {
        if (requestDTO.skillName() != null) this.skillName = requestDTO.skillName();
        if (requestDTO.category() != null) this.category = requestDTO.category();
    }

    public void replaceImage(ImageMetaDataDTO dto) {
        this.updateFile(dto);
    }
}
