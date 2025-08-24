package com.example.portfolio_website_backend.member.dto.response;

import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.member.domain.MemberSkill;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "사용자 정보 응답 DTO")
public record MemberProfileResponseDTO(

        @Schema(description = "사용자 DB index", example = "1")
        Long id,
        @Schema(description = "사용자 이름", example = "어드민")
        String name,
        @Schema(description = "사용자 정보", example = "어드민입니다.")
        String description,
        @Schema(description = "사용자 깃헙주소", example = "admin@github.com")
        String githubUrl,
        @Schema(description = "사용자 이메일주소", example = "admin@naver.com")
        String emailUrl,
        @Schema(description = "사용자 프로필 이미지 URL", example = "profile/member/~")
        String profileUrl,
        @Schema(description = "사용자 기술스택", example = "[{id : 1 , member : {Member}, skill : {Skill}, ... }, ... , ...]")
        List<MemberSkill> memberSkills

) {
    public static MemberProfileResponseDTO fromEntity(Member member) {
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
