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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<SuccessResponse<ProjectPageResponseDTO>> getAllProjects(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(SuccessResponse.ok(projectService.getAllProjects(page, size)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SuccessResponse<ProjectAddResponseDTO>> addProject(@RequestPart("data") ProjectAddRequestDTO requestDTO, @RequestPart("files") List<MultipartFile> files, @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(projectService.addProject(requestDTO, files, member)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<ProjectDeleteResponseDTO>> deleteProject(@PathVariable Long id, @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(projectService.deleteProject(id, member)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<ProjectUpdateResponseDTO>> updateProject(@PathVariable Long id, @RequestPart(value = "data", required = false) ProjectUpdateRequestDTO requestDTO, @RequestPart(value = "files", required = false) List<MultipartFile> files, @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(projectService.updateProject(id, requestDTO, files, member)));
    }
}
