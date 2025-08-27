package com.example.portfolio_website_backend.project.dto.response;

import com.example.portfolio_website_backend.project.domain.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;


@Schema(description = "프로젝트 페이지 단위 응답 DTO")
public record ProjectPageResponseDTO(
        @Schema(description = "프로젝트, 프로젝트 이미지, 프로젝트 기술스택이 담긴 리스트")
        List<ProjectResponseDTO> projects,
        @Schema(description = "총 페이지", example = "2")
        int totalPages,
        @Schema(description = "총 개수", example = "31")
        Long totalElements,
        @Schema(description = "페이지별 데이터 개수", example = "12", defaultValue = "12")
        int size,
        @Schema(description = "현재 페이지", example = "0")
        int number,
        @Schema(description = "현재 페이지가 첫 페이지인지 여부", examples = {"true","false"})
        boolean first,
        @Schema(description = "현재 페이지가 마지막 페이지인지 여부", examples = {"true", "false"})
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
