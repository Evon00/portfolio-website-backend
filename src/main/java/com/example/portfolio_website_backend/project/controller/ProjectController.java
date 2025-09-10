package com.example.portfolio_website_backend.project.controller;

import com.example.portfolio_website_backend.common.dto.SuccessResponse;
import com.example.portfolio_website_backend.common.security.annotation.CurrentMember;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.project.dto.request.ProjectAddRequestDTO;
import com.example.portfolio_website_backend.project.dto.request.ProjectFeaturedUpdateRequestDTO;
import com.example.portfolio_website_backend.project.dto.request.ProjectUpdateRequestDTO;
import com.example.portfolio_website_backend.project.dto.response.*;
import com.example.portfolio_website_backend.project.service.ProjectService;
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

import java.util.List;

@Tag(name = "Project API", description = "프로젝트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;


    @Operation(summary = "프로젝트 조회", description = "모든 프로젝트를 페이지 단위로 조회합니다.")
    @Parameters({
            @Parameter(name = "page", description = "현재 page 값, default = 0"),
            @Parameter(name = "size", description = "한 페이지의 데이터 크기, default = 6"),
            @Parameter(name = "sortBy", description = "정렬 기준, default = startDate (startDate만 가능)"),
            @Parameter(name = "sortDir", description = "정렬 방향, default = desc (desc, asc)"),
            @Parameter(name = "skillName", description = "기술 스택 이름 (optional)")
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<ProjectPageResponseDTO>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String skillName
            ) {
        return ResponseEntity.ok(SuccessResponse.ok(projectService.getAllProjects(page, size, sortBy, sortDir, skillName)));
    }

    @Operation(summary = "프로젝트 추가", description = "프로젝트 정보, 이미지 정보를 통해 프로젝트를 추가합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<ProjectAddResponseDTO>> addProject(
            @Parameter(description = "추가할 프로젝트 정보")
            @RequestPart("data") ProjectAddRequestDTO requestDTO,
            @Parameter(description = "프로젝트 이미지 파일 리스트 (jpg, png, gif, svg 지원, 최대 5MB)")
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(projectService.addProject(requestDTO, files, member)));
    }


    @Operation(summary = "프로젝트 삭제", description = "프로젝트 ID 값을 통해 해당 프로젝트를 삭제합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<ProjectDeleteResponseDTO>> deleteProject(
            @Parameter(description = "프로젝트 ID 값")
            @PathVariable Long id,
            @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(projectService.deleteProject(id, member)));
    }

    @Operation(summary = "프로젝트 수정", description = "수정할 데이터를 통해 프로젝트를 수정합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<ProjectUpdateResponseDTO>> updateProject(
            @Parameter(description = "프로젝트 ID 값")
            @PathVariable Long id,
            @Parameter(description = "수정할 프로젝트 정보 DTO")
            @RequestPart(value = "data", required = false) ProjectUpdateRequestDTO requestDTO,
            @Parameter(description = "프로젝트 이미지 파일 리스트 (jpg, png, gif, svg 지원, 최대 5MB)")
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(projectService.updateProject(id, requestDTO, files, member)));
    }

    @Operation(summary = "프로젝트 슬러그 검색", description = "keyword로 시작하는 프로젝트 슬러그를 조회합니다.")
    @GetMapping(value = "/search")
    public ResponseEntity<SuccessResponse<ProjectSlugSearchResponseDTO>> searchProjectSlug(
            @Parameter(description = "검색할 keyword")
            @RequestParam String keyword){
        return ResponseEntity.ok(SuccessResponse.ok(projectService.searchProjectSlug(keyword)));
    }

    @Operation(summary = "프로젝트 상세 조회 (슬러그)", description = "슬러그를 이용해 프로젝트 상세 페이지를 조회합니다.")
    @GetMapping(value = "/slug/{slug}")
    public ResponseEntity<SuccessResponse<ProjectResponseDTO>> getProjectDetail(
            @Parameter(description = "프로젝트 슬러그")
            @PathVariable String slug){
        return ResponseEntity.ok(SuccessResponse.ok(projectService.getProjectDetail(slug)));
    }

    @Operation(summary = "주요 프로젝트 선정", description = "프로젝트를 주요 프로젝트로 선정합니다. (최대 3개)")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/featured")
    public ResponseEntity<SuccessResponse<Void>> updateProjectFeatured(
            @Parameter(description = "주요 프로젝트로 선정할 프로젝트 ID 리스트")
            @RequestBody ProjectFeaturedUpdateRequestDTO requestDTO){
        projectService.updateProjectFeatured(requestDTO);
        return ResponseEntity.ok(SuccessResponse.ok(null));
    }

    @Operation(summary = "주요 프로젝트 조회", description = "주요 프로젝트를 조회합니다. 최대 3개")
    @GetMapping(value = "/featured")
    public ResponseEntity<SuccessResponse<ProjectFeaturedResponseDTO>> getFeaturedProjects(){
        return ResponseEntity.ok(SuccessResponse.ok(projectService.getFeaturedProjects()));
    }
}
