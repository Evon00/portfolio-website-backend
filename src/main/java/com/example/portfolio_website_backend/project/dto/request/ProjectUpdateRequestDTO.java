package com.example.portfolio_website_backend.project.dto.request;

import java.time.Instant;
import java.util.List;

public record ProjectUpdateRequestDTO(

        String title,
        String summary,
        String content,
        String githubUrl,
        String demoUrl,
        Instant startDate,
        Instant endDate,
        List<Long> skillIds,
        List<Integer> displayOrder
) {
}
