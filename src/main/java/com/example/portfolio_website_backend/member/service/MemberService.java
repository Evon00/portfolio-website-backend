package com.example.portfolio_website_backend.member.service;

import com.example.portfolio_website_backend.common.exception.BusinessException;
import com.example.portfolio_website_backend.common.security.dto.JwtMemberInfo;
import com.example.portfolio_website_backend.common.security.jwt.JwtProvider;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.member.dto.request.MemberLoginRequestDTO;
import com.example.portfolio_website_backend.member.dto.request.MemberRegisterRequestDTO;
import com.example.portfolio_website_backend.member.dto.request.MemberUpdateRequestDTO;
import com.example.portfolio_website_backend.member.dto.response.MemberLoginResponseDTO;
import com.example.portfolio_website_backend.member.dto.response.MemberProfileResponseDTO;
import com.example.portfolio_website_backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.portfolio_website_backend.common.exception.ExceptionCode.INVALID_LOGIN_CREDENTIALS;
import static com.example.portfolio_website_backend.common.exception.ExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

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

        if(passwordEncoder.matches(requestDTO.password(), member.getPassword())){
            JwtMemberInfo jwtMemberInfo = new JwtMemberInfo(member.getId(), member.getUsername(),member.getName(), member.getRole());
            return new MemberLoginResponseDTO(jwtProvider.createAccessToken(jwtMemberInfo));
        }

        throw new BusinessException(INVALID_LOGIN_CREDENTIALS);
    }

    /*
    메인 홈페이지 프로필 소개
    - [param]
    - [return] MemberProfileResponseDTO
     */

    public MemberProfileResponseDTO getMember(){
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
    public MemberProfileResponseDTO updateMember(MemberUpdateRequestDTO requestDTO, Member member){
        member.update(requestDTO);
        return MemberProfileResponseDTO.fromEntity(member);
    }
}
