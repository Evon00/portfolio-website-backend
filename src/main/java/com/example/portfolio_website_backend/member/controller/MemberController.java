package com.example.portfolio_website_backend.member.controller;

import com.example.portfolio_website_backend.common.dto.SuccessResponse;
import com.example.portfolio_website_backend.common.security.annotation.CurrentMember;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.member.dto.request.MemberLoginRequestDTO;
import com.example.portfolio_website_backend.member.dto.request.MemberSkillAddRequestDTO;
import com.example.portfolio_website_backend.member.dto.request.MemberSkillUpdateRequestDTO;
import com.example.portfolio_website_backend.member.dto.request.MemberUpdateRequestDTO;
import com.example.portfolio_website_backend.member.dto.response.MemberLoginResponseDTO;
import com.example.portfolio_website_backend.member.dto.response.MemberProfileResponseDTO;
import com.example.portfolio_website_backend.member.dto.response.MemberProfileURLResponseDTO;
import com.example.portfolio_website_backend.member.service.MemberService;
import com.example.portfolio_website_backend.skill.dto.response.SkillListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member API", description = "사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;


    /*
     - 회원가입의 경우, Admin 계정 만들기용이기에, 사용하는 일이 없을 경우 비활성화
    @Hidden
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody MemberRegisterRequestDTO requestDTO){
        return ResponseEntity.ok(SuccessResponse.ok(memberService.register(requestDTO)));
    }
     */

    @Operation(summary = "사용자 로그인", description = "ID, PW를 통해 사용자가 로그인을 합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "사용자 로그인 요청 DTO", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberLoginRequestDTO.class)))
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<MemberLoginResponseDTO>> login(@RequestBody MemberLoginRequestDTO requestDTO) {
        return ResponseEntity.ok(SuccessResponse.ok(memberService.login(requestDTO)));
    }

    @Operation(summary = "내 프로필 조회", description = "사이트 관리자의 프로필을 조회합니다.")
    @GetMapping("/profile")
    public ResponseEntity<SuccessResponse<MemberProfileResponseDTO>> viewMemberInfo() {
        return ResponseEntity.ok(SuccessResponse.ok(memberService.getMember()));
    }

    @Operation(summary = "프로필 수정", description = "관리자 권한으로 프로필 정보를 수정합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 프로필 정보", required = true,
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = MemberUpdateRequestDTO.class)))
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/profile")
    public ResponseEntity<SuccessResponse<MemberProfileResponseDTO>> updateMemberInfo(@RequestBody MemberUpdateRequestDTO requestDTO, @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(memberService.updateMember(requestDTO, member)));
    }

    @Operation(summary = "프로필 이미지 업로드", description = "관리자 권한으로 프로필 이미지를 업로드합니다.")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<MemberProfileURLResponseDTO>> uploadMemberImg(
            @Parameter(description = "프로필 이미지 파일 (jpg, png, gif, svg 지원, 최대 5MB)")
            @RequestPart("file") MultipartFile file,
            @CurrentMember Member member) {
        return ResponseEntity.ok(SuccessResponse.ok(memberService.uploadMemberProfileImg(file, member)));
    }

    @Operation(summary = "회원 기술 스택 추가", description = "로그인된 관리자의 기술 스택을 추가합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "추가할 기술 스택 ID 배열", required = true,
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = MemberSkillAddRequestDTO.class)))
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/skill")
    public ResponseEntity<SuccessResponse<SkillListResponseDTO>> addMemberSkills(@RequestBody MemberSkillAddRequestDTO requestDTO, @CurrentMember Member member){
        return ResponseEntity.ok(SuccessResponse.ok(memberService.addMemberSkills(requestDTO, member)));
    }

    @Operation(summary = "회원 기술 스택 수정", description = "로그인된 관리자의 기술 스택을 수정합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정된 기술 스택 ID 배열", required = true,
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = MemberSkillUpdateRequestDTO.class)))
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/skill")
    public ResponseEntity<SuccessResponse<SkillListResponseDTO>> updateMemberSkills(@RequestBody MemberSkillUpdateRequestDTO requestDTO, @CurrentMember Member member){
        return ResponseEntity.ok(SuccessResponse.ok(memberService.updateMemberSkills(requestDTO,member)));
    }
}
