package com.example.portfolio_website_backend.common.security.jwt;

import com.example.portfolio_website_backend.common.exception.BusinessException;
import com.example.portfolio_website_backend.common.exception.ExceptionCode;
import com.example.portfolio_website_backend.common.security.dto.JwtMemberInfo;
import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/*
UserDetailsService 커스텀
- loadUserByUsername 메서드에서 username을 통해 CustomUserDetails 반환
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        JwtMemberInfo jwtMemberInfo = new JwtMemberInfo(member.getId(), member.getUsername(), member.getName(), member.getRole());

        return new CustomUserDetails(jwtMemberInfo);

    }
}
