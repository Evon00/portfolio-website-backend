package com.example.portfolio_website_backend.project.service;

import com.example.portfolio_website_backend.common.dto.ImageMetaDataDTO;
import com.example.portfolio_website_backend.common.exception.BusinessException;
import com.example.portfolio_website_backend.common.exception.ExceptionCode;
import com.example.portfolio_website_backend.common.service.S3Uploader;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.project.domain.Project;
import com.example.portfolio_website_backend.project.domain.ProjectImage;
import com.example.portfolio_website_backend.project.dto.request.ProjectAddRequestDTO;
import com.example.portfolio_website_backend.project.dto.request.ProjectUpdateRequestDTO;
import com.example.portfolio_website_backend.project.dto.response.*;
import com.example.portfolio_website_backend.project.repository.ProjectImageRepository;
import com.example.portfolio_website_backend.project.repository.ProjectRepository;
import com.example.portfolio_website_backend.skill.domain.Skill;
import com.example.portfolio_website_backend.skill.dto.response.SkillResponseDTO;
import com.example.portfolio_website_backend.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final SkillRepository skillRepository;
    private final S3Uploader s3Uploader;

    /**
     * 프로젝트 조회
     *
     * @param page 현재 페이지 (default = 0)
     * @param size 페이지 당 데이터 개수 (default = 6)
     * @return 프로젝트 정보가 담긴 페이지 DTO
     * @return 프로젝트가 없을 시 빈 배열을 담은 DTO
     */
    public ProjectPageResponseDTO getAllProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("startDate")));
        Page<Project> projects = projectRepository.findAll(pageable);

        if (projects.isEmpty()) {
            return ProjectPageResponseDTO.createProjectPageResponseDTO(Collections.emptyList(), projects);
        }

        List<Long> projectIds = projects.getContent()
                .stream()
                .map(Project::getId)
                .toList();

        List<ProjectImage> projectImages = projectImageRepository.findByProjectIdInOrderByDisplayOrder(projectIds);

        Map<Long, List<ProjectImage>> imagesByProjectId = projectImages.stream()
                .collect(Collectors.groupingBy(
                        image -> image.getProject().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<ProjectResponseDTO> response = projects.getContent()
                .stream()
                .map(project -> {
                            List<ProjectImage> projectImageList = imagesByProjectId.getOrDefault(project.getId(), Collections.emptyList());
                            return ProjectResponseDTO.fromEntity(project, project.getProjectSkills(), projectImageList);
                        }
                ).toList();

        return ProjectPageResponseDTO.createProjectPageResponseDTO(response, projects);
    }

    /**
     * 프로젝트 추가
     *
     * @param requestDTO 추가할 프로젝트의 정보가 담긴 DTO
     * @param files      프로젝트의 이미지 리스트
     * @return 추가된 프로젝트의 일부 정보를 담은 DTO
     * @throws BusinessException 프로젝트의 기술스택이 DB에 존재하지 않을 시 발생
     */
    @Transactional
    public ProjectAddResponseDTO addProject(ProjectAddRequestDTO requestDTO, List<MultipartFile> files, Member member) {

        List<ProjectImageWithOrder> filesWithOrder = IntStream.range(0, files.size())
                .mapToObj(i -> new ProjectImageWithOrder(
                        files.get(i),
                        requestDTO.displayOrder().get(i)
                ))
                .sorted(Comparator.comparing(ProjectImageWithOrder::order))
                .toList();

        Project project = requestDTO.toEntity(member);
        projectRepository.save(project);

        List<Skill> skills = skillRepository.findAllById(requestDTO.skillIds());
        if (skills.size() != requestDTO.skillIds().size())
            throw new BusinessException(ExceptionCode.SKILL_NOT_FOUND);

        skills.forEach(project::addProjectSkill);

        saveImageToS3AndDB(filesWithOrder, requestDTO.slug(), project, requestDTO.displayOrder());

        List<SkillResponseDTO> dtos = skills.stream()
                .sorted(Comparator.comparing(Skill::getId))
                .map(SkillResponseDTO::fromEntity)
                .toList();

        return ProjectAddResponseDTO.fromEntity(project, dtos, filesWithOrder.size());

    }

    /**
     * 프로젝트 삭제
     * <p>
     * 프로젝트 삭제시 순서
     * S3 이미지 삭제 -> DB 이미지 데이터 삭제 -> 프로젝트 삭제
     *
     * @param projectId 프로젝트 ID값
     * @param member    JWT Resolver를 통해 얻은 현재 로그인된 관리자 정보
     * @return 삭제된 프로젝트 ID값
     * @throws BusinessException 해당되는 프로젝트가 없을 시 발생
     * @throws BusinessException 작성자가 아닐 시 발생
     */
    @Transactional
    public ProjectDeleteResponseDTO deleteProject(Long projectId, Member member) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new BusinessException(ExceptionCode.PROJECT_NOT_FOUND));

        if (!project.getMember().getId().equals(member.getId()))
            throw new BusinessException(ExceptionCode.INVALID_ROLE);

        List<ProjectImage> projectImages = projectImageRepository.findByProjectIdOrderByDisplayOrder(projectId);
        for (ProjectImage image : projectImages) {
            s3Uploader.delete(image.getS3Key());
        }

        projectImageRepository.deleteAll(projectImages);
        projectRepository.delete(project);

        return new ProjectDeleteResponseDTO(projectId);

    }

    /**
     * 프로젝트 수정
     *
     * @param projectId  프로젝트 ID값
     * @param requestDTO 수정할 프로젝트 정보 DTO
     * @param files      수정할 이미지 리스트
     * @param member     JWT Resolver를 통해 얻은 현재 로그인된 관리자 정보
     * @return 수정된 프로젝트의 일부 정보를 담은 DTO
     * @throws BusinessException 해당되는 프로젝트가 없을 시 발생
     * @throws BusinessException 프로젝트 작성자가 아닐 시 발생
     * @throws BusinessException 이미지 수정시 이미지 순서 정보와 이미지 정보가 일치하지 않을 시 발생
     * @throws BusinessException 해당되는 기술 스택이 없을 시 발생
     */
    @Transactional
    public ProjectUpdateResponseDTO updateProject(Long projectId, ProjectUpdateRequestDTO requestDTO, List<MultipartFile> files, Member member) {

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new BusinessException(ExceptionCode.PROJECT_NOT_FOUND));

        if (!project.getMember().getId().equals(member.getId()))
            throw new BusinessException(ExceptionCode.INVALID_ROLE);

        if (files != null && !files.isEmpty() && files.size() != requestDTO.displayOrder().size())
            throw new BusinessException(ExceptionCode.FILE_ORDER_MISMATCH);

        //project 수정
        if (requestDTO != null)
            project.update(requestDTO);
        //project 수정

        //project skill 수정
        if (requestDTO != null && requestDTO.skillIds() != null) {
            project.getProjectSkills().clear();
            List<Skill> skills = skillRepository.findAllById(requestDTO.skillIds());
            if (skills.size() != requestDTO.skillIds().size())
                throw new BusinessException(ExceptionCode.SKILL_NOT_FOUND);
            skills.forEach(project::addProjectSkill);
        }
        //project skill 수정

        //project image 수정
        if (files != null && !files.isEmpty()) {

            List<ProjectImage> existingImages = projectImageRepository.findByProjectIdOrderByDisplayOrder(projectId);
            for (ProjectImage image : existingImages) {
                s3Uploader.delete(image.getS3Key());
            }
            projectImageRepository.deleteAll(existingImages);

            List<ProjectImageWithOrder> filesWithOrder = IntStream.range(0, files.size())
                    .mapToObj(i -> new ProjectImageWithOrder(files.get(i), requestDTO.displayOrder().get(i)))
                    .sorted(Comparator.comparing(ProjectImageWithOrder::order))
                    .toList();

            saveImageToS3AndDB(filesWithOrder, project.getSlug(), project, requestDTO.displayOrder());
        }
        //project image 수정

        List<SkillResponseDTO> skillResponseDTOS = project.getProjectSkills()
                .stream()
                .map(ps -> SkillResponseDTO.fromEntity(ps.getSkill()))
                .toList();

        int imageCount = (files == null || files.isEmpty()) ? projectImageRepository.countByProjectId(projectId) : files.size();

        return ProjectUpdateResponseDTO.fromEntity(project, skillResponseDTOS, imageCount);
    }

    /**
     * 프로젝트 이미지 업로드 메서드
     *
     * @param filesWithOrder 순서가 보장된 이미지 리스트
     * @param slug           프로젝트 슬러그
     * @param project        현재 프로젝트
     * @param displayOrder   이미지 표기 순서
     */
    private void saveImageToS3AndDB(List<ProjectImageWithOrder> filesWithOrder, String slug, Project project, List<Integer> displayOrder) {
        List<String> uploadedKeys = new ArrayList<>();

        for (int i = 0; i < filesWithOrder.size(); i++) {
            try {
                ImageMetaDataDTO metaDataDTO = s3Uploader.upload(filesWithOrder.get(i).file, "project", slug);

                uploadedKeys.add(metaDataDTO.s3Key());

                ProjectImage projectImage = ProjectImage.builder()
                        .project(project)
                        .originalFilename(metaDataDTO.originalFilename())
                        .fileExtension(metaDataDTO.fileExtension())
                        .fileSize(metaDataDTO.fileSize())
                        .imageHeight(metaDataDTO.imageHeight())
                        .imageWidth(metaDataDTO.imageWidth())
                        .s3Key(metaDataDTO.s3Key())
                        .uploadUrl(metaDataDTO.uploadUrl())
                        .displayOrder(displayOrder.get(i))
                        .build();

                projectImageRepository.save(projectImage);

            } catch (IOException e) {

                for (String key : uploadedKeys) {
                    s3Uploader.delete(key);
                }

                throw new BusinessException(ExceptionCode.FAILED_IMG_UPLOAD);
            }
        }
    }

    /**
     * 프로젝트 슬러그 조회
     *
     * @param keyword 검색할 slug
     * @return keyword로 시작하는 slug를 반환
     */
    public ProjectSlugSearchResponseDTO searchProjectSlug(String keyword) {
        List<Project> projects = projectRepository.findProjectsBySlugStartingWith(keyword);
        List<String> slugs = projects.stream()
                .map(Project::getSlug)
                .toList();
        return new ProjectSlugSearchResponseDTO(slugs);
    }

    /**
     * 프로젝트 상세 조회 (슬러그)
     *
     * @param slug 상세 조회할 프로젝트의 슬러그
     * @return 해당되는 프로젝트의 상세 정보 (프로젝트, 기술 스택, 이미지)
     * @throws BusinessException 해당되는 프로젝트가 없을 시 발생
     */
    public ProjectResponseDTO getProjectDetail(String slug) {
        Project project = projectRepository.findProjectBySlug(slug).orElseThrow(
                () -> new BusinessException(ExceptionCode.PROJECT_NOT_FOUND));

        List<ProjectImage> projectImages = projectImageRepository.findByProjectIdOrderByDisplayOrder(project.getId());

        return ProjectResponseDTO.fromEntity(project,project.getProjectSkills(),projectImages);
    }

    record ProjectImageWithOrder(MultipartFile file, int order) {
    }
}
