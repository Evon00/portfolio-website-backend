package com.example.portfolio_website_backend.skill.service;

import com.example.portfolio_website_backend.common.dto.ImageMetaDataDTO;
import com.example.portfolio_website_backend.common.exception.BusinessException;
import com.example.portfolio_website_backend.common.exception.ExceptionCode;
import com.example.portfolio_website_backend.common.service.S3Uploader;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.member.domain.MemberSkill;
import com.example.portfolio_website_backend.member.repository.MemberRepository;
import com.example.portfolio_website_backend.skill.domain.Skill;
import com.example.portfolio_website_backend.skill.dto.request.SkillAddRequestDTO;
import com.example.portfolio_website_backend.skill.dto.request.SkillUpdateRequestDTO;
import com.example.portfolio_website_backend.skill.dto.response.*;
import com.example.portfolio_website_backend.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkillService {

    private final SkillRepository skillRepository;
    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;

    @Value("${personal.admin}")
    private String admin;

    /**
     * 기술스택 추가
     *
     * @param requestDTO 기술 스택 추가 요청 정보
     * @param file 기술 스택 아이콘 파일 (svg)
     * @return 생성된 기술 스택 정보
     * @throws BusinessException 이미지 업로드 실패 시 발생
     */
    @Transactional
    public SkillResponseDTO addSkill(SkillAddRequestDTO requestDTO, MultipartFile file) {
        try {
            ImageMetaDataDTO metaDataDTO = s3Uploader.upload(file, "icon", "");

            Skill skill = requestDTO.toEntity(metaDataDTO);

            return SkillResponseDTO.fromEntity(skillRepository.save(skill));

        } catch (IOException e) {
            throw new BusinessException(ExceptionCode.FAILED_IMG_UPLOAD);
        }
    }

    /**
     * 기술 스택 수정
     *
     * @param id 기술 스택 ID 값
     * @param  requestDTO 기술 스택 데이터중 변경된 내용
     * @param file 변경된 아이콘 파일 (svg)
     * @return 수정된 기술 스택 정보
     * @throws BusinessException ID 값을 통해 기술스택을 찾지 못할 시 발생
     * @throws BusinessException 아이콘 파일 업로드 실패 시 발생
     */
    @Transactional
    public SkillResponseDTO updateSkill(Long id, SkillUpdateRequestDTO requestDTO, MultipartFile file) {
        Skill skill = skillRepository.findById(id).orElseThrow(() ->
                new BusinessException(ExceptionCode.SKILL_NOT_FOUND));

        boolean hasFileUpdate = file != null && !file.isEmpty();

        if (hasFileUpdate) {
            s3Uploader.delete(skill.getS3Key());
            try {
                ImageMetaDataDTO metaDataDTO = s3Uploader.upload(file, "icon", "");
                skill.replaceImage(metaDataDTO);
            } catch (IOException e) {
                throw new BusinessException(ExceptionCode.FAILED_IMG_UPLOAD);
            }
        }
        if (requestDTO != null) skill.update(requestDTO);
        return SkillResponseDTO.fromEntity(skill);
    }

    /**
     * 기술 스택 삭제
     *
     * @param id 기술 스택 ID 값
     * @return 삭제된 기술 스택 ID 값
     * @throws BusinessException ID 값을 통해 기술스택을 찾지 못할 시 발생
     */
    @Transactional
    public SkillDeleteResponseDTO deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() ->
                new BusinessException(ExceptionCode.SKILL_NOT_FOUND));
        s3Uploader.delete(skill.getS3Key());
        skillRepository.deleteById(skill.getId());
        return new SkillDeleteResponseDTO(skill.getId());
    }


    /**
     * 기술 스택 조회
     *
     * @param page 조회시 page 값 (default = 0)
     * @param  size 조회시 size 값 (default = 12)
     * @return 기술 스택 page 반환
     */
    public SkillPageResponseDTO getAllSkills(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Skill> skillPage = skillRepository.findAllByOrderById(pageable);
        List<SkillResponseDTO> dtoList = skillPage.getContent()
                .stream()
                .map(SkillResponseDTO::fromEntity)
                .toList();

        return SkillPageResponseDTO.createSkillPageResponseDTO(dtoList, skillPage);

    }

    /**
     * 기술스택 조회 (카테고리별)
     *
     * @param category 조회하고자 하는 카테고리
     * @param page 조회시 page 값 (default = 0)
     * @param  size 조회시 size 값 (default = 12)
     * @return 기술 스택 page 반환
     */
    public SkillPageResponseDTO getSkillsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Skill> skillPage = skillRepository.findByCategoryOrderById(category, pageable);
        List<SkillResponseDTO> dtoList = skillPage.getContent()
                .stream()
                .map(SkillResponseDTO::fromEntity)
                .toList();
        return SkillPageResponseDTO.createSkillPageResponseDTO(dtoList, skillPage);
    }

    /**
     * 기술 스택 카테고리 조회
     *
     * @return 기술 스택 카테고리 반환
     */
    public SkillCategoryResponseDTO getSkillCategory() {
        List<String> categories = skillRepository.findDistinctByCategories();
        return new SkillCategoryResponseDTO(categories);
    }

    /**
     * 기술 스택 검색
     *
     * @param keyword 검색하고자 하는 단어
     * @return keyword로 시작하는 기술 스택 반환
     */
    public SkillListResponseDTO searchSkillByKeyword(String keyword) {
        List<Skill> skills = skillRepository.findSkillsBySkillNameStartingWithIgnoreCaseOrderBySkillNameAsc(keyword);
        List<SkillResponseDTO> dto = skills
                .stream()
                .map(SkillResponseDTO::fromEntity)
                .toList();
        return new SkillListResponseDTO(dto);
    }

    /**
     * 사이트 관리자 기술스택 조회
     *
     * @return 관리자의 기술 스택 반환
     */
    public SkillListResponseDTO getAdminSkills() {
        Member member = memberRepository.findByUsername(admin).orElseThrow(
                () -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        List<MemberSkill> memberSkills = member.getMemberSkills();

        List<SkillResponseDTO> dto = memberSkills
                .stream()
                .sorted(Comparator.comparing(memberSkill -> memberSkill.getSkill().getId()))
                .map(memberSkill -> SkillResponseDTO.fromEntity(memberSkill.getSkill()))
                .toList();

        return new SkillListResponseDTO(dto);

    }
}
