package com.example.portfolio_website_backend.skill.controller;

import com.example.portfolio_website_backend.common.dto.SuccessResponse;
import com.example.portfolio_website_backend.skill.dto.request.SkillAddRequestDTO;
import com.example.portfolio_website_backend.skill.dto.request.SkillUpdateRequestDTO;
import com.example.portfolio_website_backend.skill.dto.response.*;
import com.example.portfolio_website_backend.skill.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Skill API", description = "기술 스택 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skill")
public class SkillController {

    private final SkillService skillService;


    @Operation(summary = "기술 스택 추가", description = "기술 스택 이름, 카테고리, 파일을 통해 기술 스택을 추가합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<SkillResponseDTO>> addSkill(
            @Parameter(description = "기술 스택 정보")
            @RequestPart("data") SkillAddRequestDTO requestDTO,
            @Parameter(description = "기술 스택 아이콘 파일 (jpg, png, gif, svg 지원, 최대 5MB)")
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.addSkill(requestDTO, file)));
    }

    @Operation(summary = "기술 스택 수정", description = "변경된 정보를 통해 기술 스택을 수정합니다.")
    @Parameters({
            @Parameter(name = "id", description = "기술 스택 ID값", required = true),
    })
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<SkillResponseDTO>> updateSkill(
            @PathVariable Long id,
            @Parameter(description = "기술 스택 정보")
            @RequestPart(value = "data", required = false) SkillUpdateRequestDTO requestDTO,
            @Parameter(description = "기술 스택 아이콘 파일 (jpg, png, gif, svg 지원, 최대 5MB)")
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.updateSkill(id, requestDTO, file)));
    }

    @Operation(summary = "기술 스택 삭제", description = "해당되는 기술 스택을 삭제합니다.")
    @Parameter(name = "id", description = "기술 스택 ID값", required = true)
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<SkillDeleteResponseDTO>> deleteSkill(@PathVariable Long id) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.deleteSkill(id)));
    }

    @Operation(summary = "기술 스택 조회", description = "모든 기술 스택을 페이지 단위로 조회합니다.")
    @Parameters({
            @Parameter(name = "page", description = "현재 page 값, default = 0"),
            @Parameter(name = "size", description = "한 페이지의 데이터 크기, default = 12")
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<SkillPageResponseDTO>> getAllSkills(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.getAllSkills(page, size)));
    }

    @Operation(summary = "기술 스택 조회 - 카테고리별", description = "해당 카테고리의 기술 스택을 페이지 단위로 조회합니다.")
    @Parameters({
            @Parameter(name = "category", description = "카테고리 명", required = true),
            @Parameter(name = "page", description = "현재 page 값, default = 0"),
            @Parameter(name = "size", description = "한 페이지의 데이터 크기, default = 12")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<SuccessResponse<SkillPageResponseDTO>> getSkillsByCategory(@PathVariable String category, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.getSkillsByCategory(category, page, size)));
    }

    @Operation(summary = "기술 스택 카테고리 목록 조회", description = "현재 등록된 기술 스택의 카테고리를 조회합니다.")
    @GetMapping("/category")
    public ResponseEntity<SuccessResponse<SkillCategoryResponseDTO>> getSkillCategory() {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.getSkillCategory()));
    }

    @Operation(summary = "기술 스택 검색", description = "키워드로 시작하는 기술 스택을 검색합니다.")
    @Parameter(name = "keyword", description = "검색할 단어", required = true)
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<SkillListResponseDTO>> searchSkillByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.searchSkillByKeyword(keyword)));
    }

    @Operation(summary = "사이트 관리자 기술 스택 조회", description = "현재 사이트 관리자의 기술 스택을 조회합니다.")
    @GetMapping("/admin")
    public ResponseEntity<SuccessResponse<SkillListResponseDTO>> getAdminSkills() {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.getAdminSkills()));
    }
}
