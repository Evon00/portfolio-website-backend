package com.example.portfolio_website_backend.member.dto.request;

import com.example.portfolio_website_backend.common.domain.Role;
import com.example.portfolio_website_backend.member.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

public record MemberRegisterRequestDTO(
        String username,
        String password,
        String name,
        String description,
        String githubUrl,
        String emailUrl,
        String profileUrl
) {
    public Member toEntity(PasswordEncoder passwordEncoder){
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
