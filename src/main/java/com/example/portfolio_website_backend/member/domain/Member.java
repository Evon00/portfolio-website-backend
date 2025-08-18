package com.example.portfolio_website_backend.member.domain;

import com.example.portfolio_website_backend.common.domain.Role;
import com.example.portfolio_website_backend.member.dto.request.MemberUpdateRequestDTO;
import com.example.portfolio_website_backend.skill.domain.Skill;
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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "email_url")
    private String emailUrl;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "s3_key")
    private String s3Key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    List<MemberSkill> memberSkills = new ArrayList<>();
    @Builder
    public Member(String username, String password, String name, String description, String githubUrl, String emailUrl, String profileUrl, String s3Key, Role role){
        this.username = username;
        this.password = password;
        this.name = name;
        this.description = description;
        this.githubUrl = githubUrl;
        this.emailUrl = emailUrl;
        this.profileUrl = profileUrl;
        this.s3Key = s3Key;
        this.role = role;
    }

    public void addMemberSkill(Skill skill){
        MemberSkill memberSkill = MemberSkill.builder()
                .member(this)
                .skill(skill)
                .build();
        memberSkills.add(memberSkill);
    }

    public void removeMemberSkill(MemberSkill memberSkill) {
        this.memberSkills.remove(memberSkill);
    }

    public void update(MemberUpdateRequestDTO requestDTO){
        if(requestDTO.name() != null) this.name = requestDTO.name();
        if(requestDTO.description() != null) this.description = requestDTO.description();
        if(requestDTO.githubUrl() != null) this.githubUrl = requestDTO.githubUrl();
        if(requestDTO.emailUrl() != null) this.emailUrl = requestDTO.emailUrl();
        if(requestDTO.profileUrl() != null) this.profileUrl = requestDTO.profileUrl();
        if(requestDTO.s3Key() != null) this.s3Key = requestDTO.s3Key();
    }

    public void setImageNull(){
        this.profileUrl = null;
        this.s3Key = null;
    }
}
