package com.example.portfolio_website_backend.post.service;

import com.example.portfolio_website_backend.common.dto.ImageMetaDataDTO;
import com.example.portfolio_website_backend.common.exception.BusinessException;
import com.example.portfolio_website_backend.common.exception.ExceptionCode;
import com.example.portfolio_website_backend.common.service.S3Uploader;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.post.domain.Post;
import com.example.portfolio_website_backend.post.domain.PostImage;
import com.example.portfolio_website_backend.post.dto.request.PostAddRequestDTO;
import com.example.portfolio_website_backend.post.dto.request.PostUpdateRequestDTO;
import com.example.portfolio_website_backend.post.dto.response.*;
import com.example.portfolio_website_backend.post.repository.PostImageRepository;
import com.example.portfolio_website_backend.post.repository.PostRepository;
import com.example.portfolio_website_backend.skill.domain.Skill;
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
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final SkillRepository skillRepository;

    private final S3Uploader s3Uploader;

    /**
     * 게시글 이미지 업로드
     *
     * 게시글 본문에 들어갈 이미지를 업로드하는 메서드
     *
     * @param file 게시글 이미지
     * @param slug 게시글 슬러그
     * @return 업로드된 이미지의 정보가 담긴 DTO
     * @throws BusinessException 이미지 업로드 실패시 발생
     */
    @Transactional
    public PostImageResponseDTO uploadPostImage(MultipartFile file, String slug) {
        try {
            ImageMetaDataDTO metaDataDTO = s3Uploader.upload(file, "post", slug);
            PostImage postImage = PostImage.builder()
                    .originalFilename(metaDataDTO.originalFilename())
                    .uploadUrl(metaDataDTO.uploadUrl())
                    .s3Key(metaDataDTO.s3Key())
                    .imageWidth(metaDataDTO.imageWidth())
                    .imageHeight(metaDataDTO.imageHeight())
                    .fileSize(metaDataDTO.fileSize())
                    .fileExtension(metaDataDTO.fileExtension())
                    .build();
            postImageRepository.save(postImage);
            return PostImageResponseDTO.create(postImage);
        } catch (IOException e) {
            throw new BusinessException(ExceptionCode.FAILED_IMG_UPLOAD);
        }
    }

    /**
     * 게시글 추가
     *
     * 게시글 추가 -> 게시글 - 기술스택 연관관계 추가 -> 게시글 - 이미지 연관관계 추가
     *
     * @param requestDTO 게시글 추가 정보 DTO
     * @param member JWT Resolver를 통해 얻은 현재 로그인된 관리자 정보
     * @return 추가된 게시글 요약 정보를 반환
     * @throws BusinessException 기술 스택이 존재하지 않을 시 발생
     * @throws BusinessException 이미지가 존재하지 않을 시 발생
     */
    @Transactional
    public PostResponseDTO addPost(PostAddRequestDTO requestDTO, Member member) {

        List<Skill> skills = skillRepository.findAllById(requestDTO.skills());
        if (skills.size() != requestDTO.skills().size())
            throw new BusinessException(ExceptionCode.SKILL_NOT_FOUND);

        Post post = requestDTO.toEntity(member);
        skills.forEach(post::addPostSkill);
        postRepository.save(post);
        
        if (!requestDTO.images().isEmpty()) {
            List<PostImage> postImages = postImageRepository.findAllById(requestDTO.images());
            if (postImages.size() != requestDTO.images().size())
                throw new BusinessException(ExceptionCode.IMAGE_NOT_FOUND);

            postImages.forEach(postImage -> postImage.setPost(post));
            postImageRepository.saveAll(postImages);
        }

        return PostResponseDTO.create(post);
    }

    /**
     * 게시글 조회 (페이지 단위)
     * 
     * @param page 현재 페이지
     * @param size 페이지의 데이터 개수
     * @param sortBy 정렬 기준 (createdAt(작성 시간), View(조회수))
     * @param sortDir 정렬 방향 (ASC, DESC)
     * @param skill 게시글 기술 스택 필터링 (Optional)
     * @return 페이지 단위의 게시글 반환
     */
    public PostPageResponseDTO getAllPosts(int page, int size, String sortBy, String sortDir, String skill) {

        List<String> allowedSortBy = List.of("createdAt", "view");
        List<String> allowedSortDir = List.of("asc", "desc");

        if (!allowedSortBy.contains(sortBy)) sortBy = "createdAt";
        if (!allowedSortDir.contains(sortDir.toLowerCase())) sortDir = "desc";

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> postPage;

        if(skill != null && !skill.isEmpty()){
            postPage = postRepository.findBySkillName(skill, pageable);
        }else {
            postPage = postRepository.findAll(pageable);
        }

        if (postPage.isEmpty())
            return PostPageResponseDTO.create(Collections.emptyList(), postPage);

        List<PostResponseDTO> postResponseDTOS = postPage.stream()
                .map(PostResponseDTO::create)
                .toList();

        return PostPageResponseDTO.create(postResponseDTOS, postPage);
    }

    /**
     * 게시글 수정
     *
     * @param id 게시글 ID값
     * @param requestDTO 수정할 게시글 정보
     * @param member JWT Resolver를 통해 얻은 현재 로그인된 관리자 정보
     * @return 수정된 게시글의 요약된 정보를 반환
     * @throws BusinessException 해당되는 게시글이 없을 시 발생
     * @throws BusinessException 게시글의 작성자가 아닐 시 발생
     * @throws BusinessException 해당되는 기술 스택이 없을 시 발생
     * @throws BusinessException 해당되는 이미지가 없을 시 발생
     */
    @Transactional
    public PostResponseDTO updatePost(Long id, PostUpdateRequestDTO requestDTO, Member member) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new BusinessException(ExceptionCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(member.getId()))
            throw new BusinessException(ExceptionCode.INVALID_ROLE);

        //게시글 수정
        if (requestDTO != null) {
            post.update(requestDTO);
        }
        //게시글 수정

        //기술 스택 수정
        if (requestDTO != null && requestDTO.skills() != null) {
            post.getPostSkills().clear();
            List<Skill> skills = skillRepository.findAllById(requestDTO.skills());
            if (skills.size() != requestDTO.skills().size())
                throw new BusinessException(ExceptionCode.SKILL_NOT_FOUND);
            skills.forEach(post::addPostSkill);
        }
        //기술 스택 수정

        //이미지 수정
        if (requestDTO != null && requestDTO.images() != null) {
            List<PostImage> postImages = postImageRepository.findPostImagesByPostId(id);
            if (!postImages.isEmpty()) {
                postImages.forEach(pi -> s3Uploader.delete(pi.getS3Key()));
                postImageRepository.deletePostImageByPostId(id);
            }

            List<PostImage> addedPostImage = postImageRepository.findAllById(requestDTO.images());
            if (postImages.size() != requestDTO.images().size())
                throw new BusinessException(ExceptionCode.IMAGE_NOT_FOUND);

            addedPostImage.forEach(postImage -> postImage.setPost(post));
        }
        //이미지 수정

        return PostResponseDTO.create(post);

    }

    /**
     * 게시글 삭제
     *
     * @param id 게시글 ID값
     * @param member JWT Resolver를 통해 얻은 현재 로그인된 관리자 정보
     * @return 삭제된 게시글 ID값
     * @throws BusinessException 해당되는 게시글이 없을 시 발생
     * @throws BusinessException 게시글 작성자가 아닐 시 발생
     */
    @Transactional
    public PostDeleteResponseDTO deletePost(Long id, Member member) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new BusinessException(ExceptionCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(member.getId()))
            throw new BusinessException(ExceptionCode.INVALID_ROLE);

        //이미지 삭제
        List<PostImage> postImages = postImageRepository.findPostImagesByPostId(post.getId());
        postImages.forEach(pi -> s3Uploader.delete(pi.getS3Key()));
        postImageRepository.deleteAll(postImages);
        //이미지 삭제

        //게시글 삭제
        postRepository.delete(post);
        //게시글 삭제

        return new PostDeleteResponseDTO(post.getId());
    }

    /**
     * 게시글 상세 조회 - 슬러그
     * 
     * @param slug 게시글 슬러그
     * @return 해당 되는 게시글의 상세 정보
     * @throws BusinessException 게시글이 없을 시 발생
     */
    public PostDetailResponseDTO getPostDetail(String slug) {
        Post post = postRepository.findPostBySlug(slug).orElseThrow(
                () -> new BusinessException(ExceptionCode.POST_NOT_FOUND));

        return PostDetailResponseDTO.create(post);
    }

    /**
     * 게시글 슬러그 검색
     * 
     * @param keyword 검색할 슬러그
     * @return 해당 단어로 시작하는 슬러그 반환
     */
    public PostSlugSearchResponseDTO searchPostSlug(String keyword) {
        List<Post> posts = postRepository.findPostBySlugStartingWithOrderBySlug(keyword);

        List<String> slugs = posts.stream()
                .map(Post::getSlug)
                .toList();

        return new PostSlugSearchResponseDTO(slugs);
    }
}
