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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${personal.admin}")
    private String admin;

    /**
     *  회원가입
     *
     * @param requestDTO 회원가입 요청 DTO
     * @return Member 객체
     */
    @Transactional
    public Member register(MemberRegisterRequestDTO requestDTO) {

        Member member = requestDTO.toEntity(passwordEncoder);
        return memberRepository.save(member);

    }

    /**
     *  로그인
     *
     * @param requestDTO 로그인 요청 DTO
     * @return 로그인된 회원 정보 DTO
     * @throws BusinessException 해당 회원이 존재하지 않을 시 발생
     * @throws BusinessException 아이디, 비밀번호 중 잘못된 것이 있을 시 발생
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

    /**
     * 메인 홈페이지 프로필 소개 - 관리자
     *
     * @return 회원 관련 정보를 담은 DTO
     * @throws BusinessException 해당 회원이 존재하지 않을 시 발생
     */
    public MemberProfileResponseDTO getMember() {
        Member member = memberRepository.findByUsername(admin).orElseThrow(
                () -> new BusinessException(USER_NOT_FOUND)
        );

        return MemberProfileResponseDTO.fromEntity(member);

    }

    /**
     * 프로필 수정
     *
     * @param requestDTO 회원 프로필 정보중 수정된 내용을 담은 DTO
     * @param member JWT Resolver를 통해 얻은 현재 로그인된 관리자 정보
     * @return 수정된 내용이 적용된 회원 프로필 정보
     */
    @Transactional
    public MemberProfileResponseDTO updateMember(MemberUpdateRequestDTO requestDTO, Member member) {
        member.update(requestDTO);
        return MemberProfileResponseDTO.fromEntity(member);
    }

    /**
     * 회원 프로필 이미지 업로드
     *
     * @param file MultipartFile 형태의 이미지
     * @param member JWT Resolver를 통해 얻은 현재 로그인된 관리자 정보
     * @return 회원 프로필이 업로드된 cloudFront URL를 담은 DTO
     * @throws BusinessException 이미지 업로드 실패시 발생
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

    /**
     * 회원 기술 스택 추가
     *
     * @param requestDTO 추가할 기술 스택 ID 배열
     * @param member JWT Resolver를 통해 얻은 현재 로그인된 관리자 정보
     * @return 추가한 기술 스택 정보를 담은 DTO
     * @throws BusinessException 추가할 기술 스택이 존재하지 않을 시 발생
     * @throws BusinessException 추가할 기술 스택이 이미 추가되었을 시 발생
     */
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

    /**
     * 회원 기술 스택 수정
     *
     * @param requestDTO 수정된 기술스택 ID 배열
     * @param member JWT Resolver를 통해 얻은 현재 로그인된 관리자 정보
     * @return 수정된 기술 스택 정보를 담은 DTO
     * @throws BusinessException 추가할 기술 스택이 존재하지 않을 시 발생
     */
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
