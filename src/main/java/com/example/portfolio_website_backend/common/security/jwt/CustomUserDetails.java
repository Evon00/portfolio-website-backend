package com.example.portfolio_website_backend.common.security.jwt;

import com.example.portfolio_website_backend.common.security.dto.JwtMemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/*
UserDetails 커스텀
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final JwtMemberInfo jwtMemberInfo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(jwtMemberInfo.role().name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return jwtMemberInfo.username();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
