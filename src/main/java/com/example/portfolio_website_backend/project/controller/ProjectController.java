package com.example.portfolio_website_backend.project.controller;

import com.example.portfolio_website_backend.common.dto.SuccessResponse;
import com.example.portfolio_website_backend.common.security.annotation.CurrentMember;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.project.dto.request.ProjectAddRequestDTO;
import com.example.portfolio_website_backend.project.dto.request.ProjectUpdateRequestDTO;
import com.example.portfolio_website_backend.project.dto.response.ProjectAddResponseDTO;
import com.example.portfolio_website_backend.project.dto.response.ProjectDeleteResponseDTO;
import com.example.portfolio_website_backend.project.dto.response.ProjectPageResponseDTO;
import com.example.portfolio_website_backend.project.dto.response.ProjectUpdateResponseDTO;
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
            @Parameter(name = "size", description = "한 페이지의 데이터 크기, default = 6")
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<ProjectPageResponseDTO>> getAllProjects(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(SuccessResponse.ok(projectService.getAllProjects(page, size)));
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
}
