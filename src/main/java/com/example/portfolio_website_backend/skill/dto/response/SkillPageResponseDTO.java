package com.example.portfolio_website_backend.skill.dto.response;

import com.example.portfolio_website_backend.skill.domain.Skill;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "기술 스택 페이지 반환 DTO")
public record SkillPageResponseDTO(

        @Schema(description = "기술 스택 반환 DTO")
        List<SkillResponseDTO> skills,

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

    public static SkillPageResponseDTO createSkillPageResponseDTO(List<SkillResponseDTO> skillList, Page<Skill> page) {
        return new SkillPageResponseDTO(
                skillList,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.isFirst(),
                page.isLast()
        );
    }
}
