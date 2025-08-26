package com.example.portfolio_website_backend.project.dto.response;

import com.example.portfolio_website_backend.project.domain.Project;
import org.springframework.data.domain.Page;

import java.util.List;


public record ProjectPageResponseDTO(
        List<ProjectResponseDTO> projects,
        int totalPages,
        Long totalElements,
        int size,
        int number,
        boolean first,
        boolean last

) {

    public static ProjectPageResponseDTO createProjectPageResponseDTO(List<ProjectResponseDTO> projects, Page<Project> page) {
        return new ProjectPageResponseDTO(
                projects,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.isFirst(),
                page.isLast()
        );
    }
}
