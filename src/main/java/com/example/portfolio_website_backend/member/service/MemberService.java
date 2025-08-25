package com.example.portfolio_website_backend.member.service;

import com.example.portfolio_website_backend.common.dto.ImageMetaDataDTO;
import com.example.portfolio_website_backend.common.exception.BusinessException;
import com.example.portfolio_website_backend.common.security.dto.JwtMemberInfo;
import com.example.portfolio_website_backend.common.security.jwt.JwtProvider;
import com.example.portfolio_website_backend.common.service.S3Uploader;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.member.dto.request.*;
import com.example.portfolio_website_backend.member.dto.response.MemberLoginResponseDTO;
import com.example.portfolio_website_backend.member.dto.response.MemberProfileResponseDTO;
import com.example.portfolio_website_backend.member.dto.response.MemberProfileURLResponseDTO;
import com.example.portfolio_website_backend.member.repository.MemberRepository;
import com.example.portfolio_website_backend.member.repository.MemberSkillRepository;
import com.example.portfolio_website_backend.skill.domain.Skill;
import com.example.portfolio_website_backend.skill.dto.response.SkillListResponseDTO;
import com.example.portfolio_website_backend.skill.dto.response.SkillResponseDTO;
import com.example.portfolio_website_backend.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.portfolio_website_backend.common.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final MemberSkillRepository memberSkillRepository;
    private final JwtProvider jwtProvider;
    private final S3Uploader s3Uploader;

    /*
    회원가입
    - [param] 회원가입 정보 DTO
    - [return] Member 객체
     */
    @Transactional
    public Member register(MemberRegisterRequestDTO requestDTO) {

        Member member = requestDTO.toEntity(passwordEncoder);
        return memberRepository.save(member);

    }

    /*
    로그인
    - [param] 로그인 정보 DTO
    - [return] JWT
     */

    public MemberLoginResponseDTO login(MemberLoginRequestDTO requestDTO) {
        Member member = memberRepository.findByUsername(requestDTO.username()).orElseThrow(
                () -> new BusinessException(USER_NOT_FOUND)
        );

        if (passwordEncoder.matches(requestDTO.password(), member.getPassword())) {
            JwtMemberInfo jwtMemberInfo = new JwtMemberInfo(member.getId(), member.getUsername(), member.getName(), member.getRole());
            return new MemberLoginResponseDTO(jwtProvider.createAccessToken(jwtMemberInfo));
        }

        throw new BusinessException(INVALID_LOGIN_CREDENTIALS);
    }

    /*
    메인 홈페이지 프로필 소개
    - [param]
    - [return] MemberProfileResponseDTO
     */

    public MemberProfileResponseDTO getMember() {
        Member member = memberRepository.findById(1L).orElseThrow(
                () -> new BusinessException(USER_NOT_FOUND)
        );

        return MemberProfileResponseDTO.fromEntity(member);

    }

    /*
    프로필 수정
    - [param] MemberUpdateRequestDTO(Patch) , Member
    - [return] MemberProfileResponseDTO
     */
    @Transactional
    public MemberProfileResponseDTO updateMember(MemberUpdateRequestDTO requestDTO, Member member) {
        member.update(requestDTO);
        return MemberProfileResponseDTO.fromEntity(member);
    }

    /*
    회원 프로필 이미지 업로드
    - [param] MultiparFile(사진), Member
    - [return] MemberProfilieURLResponseDTO
    - 파일 업로드시 기존 이미지가 존재한다면 해당 이미지를 삭제하고 새로 업로드 하는 방식
     */
    @Transactional
    public MemberProfileURLResponseDTO uploadMemberProfileImg(MultipartFile file, Member member) {
        ImageMetaDataDTO dto = null;

        try {

            if (member.getProfileUrl() != null) {
                s3Uploader.delete(member.getS3Key());
                member.setImageNull();
            }
            dto = s3Uploader.upload(file, "profile", member.getUsername());

        } catch (IOException e) {
            throw new BusinessException(FAILED_IMG_UPLOAD);
        }

        if (dto != null) {
            return new MemberProfileURLResponseDTO(dto.uploadUrl(), dto.s3Key());
        } else return null;

    }

    @Transactional
    public SkillListResponseDTO addMemberSkills(MemberSkillAddRequestDTO requestDTO, Member member) {

        List<Skill> skills = skillRepository.findAllById(requestDTO.skillIds());
        if(skills.size() != requestDTO.skillIds().size())
            throw new BusinessException(SKILL_NOT_FOUND);


        List<Long> existingSkills = memberSkillRepository.findByMember(member)
                .stream()
                .map(memberSkill -> memberSkill.getSkill().getId())
                .toList();

        List<Skill> newSkills = skills
                .stream()
                .filter(skill -> !existingSkills.contains(skill.getId()))
                .toList();

        if(newSkills.isEmpty())
            throw new BusinessException(ALL_SKILLS_ALREADY_ADDED);

        newSkills.forEach(member::addMemberSkill);
        memberRepository.save(member);

        List<SkillResponseDTO> dtos = newSkills.stream()
                .sorted(Comparator.comparing(Skill::getId))
                .map(SkillResponseDTO::fromEntity)
                .toList();

        return new SkillListResponseDTO(dtos);
    }

    @Transactional
    public SkillListResponseDTO updateMemberSkills(MemberSkillUpdateRequestDTO requestDTO, Member member) {
        member.getMemberSkills().clear();
        memberSkillRepository.deleteByMember(member);

        if(requestDTO.skillIds().isEmpty())
            return new SkillListResponseDTO(Collections.emptyList());

        List<Skill> newSkills = skillRepository.findAllById(requestDTO.skillIds());
        if(newSkills.size() != requestDTO.skillIds().size())
            throw new BusinessException(SKILL_NOT_FOUND);

        newSkills.forEach(member::addMemberSkill);
        memberRepository.save(member);

        List<SkillResponseDTO> dtos = newSkills.stream()
                .sorted(Comparator.comparing(Skill::getId))
                .map(SkillResponseDTO::fromEntity)
                .toList();

        return new SkillListResponseDTO(dtos);
    }
}
