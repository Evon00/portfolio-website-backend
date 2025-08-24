package com.example.portfolio_website_backend.skill.controller;

import com.example.portfolio_website_backend.common.dto.SuccessResponse;
import com.example.portfolio_website_backend.skill.dto.request.SkillAddRequestDTO;
import com.example.portfolio_website_backend.skill.dto.request.SkillUpdateRequestDTO;
import com.example.portfolio_website_backend.skill.dto.response.*;
import com.example.portfolio_website_backend.skill.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skill")
public class SkillController {

    private final SkillService skillService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SuccessResponse<SkillResponseDTO>> addSkill(@RequestPart("data") SkillAddRequestDTO requestDTO, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.addSkill(requestDTO, file)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<SkillResponseDTO>> updateSkill(@PathVariable Long id, @RequestPart(value = "data", required = false) SkillUpdateRequestDTO requestDTO, @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.updateSkill(id, requestDTO, file)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SuccessResponse<SkillDeleteResponseDTO>> deleteSkill(@PathVariable Long id) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.deleteSkill(id)));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<SkillPageResponseDTO>> getAllSkills(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.getAllSkills(page, size)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<SuccessResponse<SkillPageResponseDTO>> getSkillsByCategory(@PathVariable String category, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.getSkillsByCategory(category, page, size)));
    }

    @GetMapping("/category")
    public ResponseEntity<SuccessResponse<SkillCategoryResponseDTO>> getSkillCategory() {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.getSkillCategory()));
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<SkillListResponseDTO>> searchSkillByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.searchSkillByKeyword(keyword)));
    }

    @GetMapping("/admin")
    public ResponseEntity<SuccessResponse<SkillListResponseDTO>> getAdminSkills() {
        return ResponseEntity.ok(SuccessResponse.ok(skillService.getAdminSkills()));
    }
}
