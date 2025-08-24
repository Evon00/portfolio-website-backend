package com.example.portfolio_website_backend.member.dto.request;

import com.example.portfolio_website_backend.common.domain.Role;
import com.example.portfolio_website_backend.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.crypto.password.PasswordEncoder;

@Schema(description = "회원가입 요청 DTO")
public record MemberRegisterRequestDTO(

        @Schema(description = "사용자 ID", example = "admin")
        String username,
        @Schema(description = "사용자 PW", example = "admin123")
        String password,
        @Schema(description = "사용자 이름", example = "어드민")
        String name,
        @Schema(description = "회원에 대한 설명", example = "저는 관리자입니다.")
        String description,
        @Schema(description = "사용자 깃헙주소", example = "admin@github.com")
        String githubUrl,
        @Schema(description = "사용자 이메일주소", example = "admin@naver.com")
        String emailUrl,
        @Schema(description = "사용자 프로필 이미지 URL", example = "123/profile/member/~")
        String profileUrl
) {
    public Member toEntity(PasswordEncoder passwordEncoder) {
        Role role = Role.ADMIN;

        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .description(description)
                .githubUrl(githubUrl)
                .emailUrl(emailUrl)
                .profileUrl(profileUrl)
                .s3Key(null)
                .role(role)
                .build();
    }
}
