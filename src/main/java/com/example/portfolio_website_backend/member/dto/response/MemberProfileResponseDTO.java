package com.example.portfolio_website_backend.member.dto.response;

import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.member.domain.MemberSkill;

import java.util.List;

public record MemberProfileResponseDTO(
        Long id,
        String name,
        String description,
        String githubUrl,
        String emailUrl,
        String profileUrl,
        List<MemberSkill> memberSkills
) {
    public static MemberProfileResponseDTO fromEntity(Member member){
        return new MemberProfileResponseDTO(
                member.getId(),
                member.getName(),
                member.getDescription(),
                member.getGithubUrl(),
                member.getEmailUrl(),
                member.getProfileUrl(),
                member.getMemberSkills()
        );
    }
}
