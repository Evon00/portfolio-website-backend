package com.example.portfolio_website_backend.common.security.dto;

import com.example.portfolio_website_backend.common.domain.Role;

/*
JWT payload 포함할 정보 객체
 */
public record JwtMemberInfo(
        Long memberId,
        String username,
        String name,
        Role role
) {
}
