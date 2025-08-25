package com.example.portfolio_website_backend.member.dto.request;

import java.util.List;

public record MemberSkillUpdateRequestDTO(
        List<Long> skillIds
) {
}
