package com.example.portfolio_website_backend.project.dto.request;

import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.project.domain.Project;

import java.time.Instant;
import java.util.List;

public record ProjectAddRequestDTO(

        String title,
        String summary,
        String content,
        String githubUrl,
        String demoUrl,
        Instant startDate,
        Instant endDate,
        List<Long> skillIds,
        List<Integer> displayOrder,
        String slug
) {
    public Project toEntity(Member member) {
        return Project.builder()
                .member(member)
                .title(this.title)
                .summary(this.summary)
                .content(this.content)
                .githubUrl(this.githubUrl)
                .demoUrl(this.demoUrl)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .slug(this.slug)
                .build();
    }
}
