package com.example.portfolio_website_backend.common.security.jwt;


import com.example.portfolio_website_backend.common.exception.ExceptionResponse;
import com.example.portfolio_website_backend.common.security.exception.AuthException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailService userDetailService;
    private final ObjectMapper objectMapper;

    private final List<String> ignoreUrls = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/favicon.ico",
            "/api-docs",
            "/v3/api-docs",
            "/swagger-ui",
            "/css",
            "/images",
            "/js",
            "/swagger"
    );

    /*
    JWT 필터 사용하지 않는 곳
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return ignoreUrls.stream()
                .anyMatch(requestURI::startsWith);
    }


    /*
    요청 내부 필터
    - [param] : Request, Response, filterChain(필터)
    - [return] : void
    - JWT 여부 확인후, JWT가 존재하면 인증 객체 생성 및 등록 이후 다음 필터로 이동,
      JWT가 없는 경우, 바로 다음 필터로 이동
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            String accessToken = resolveToken(request);

            if (accessToken != null && jwtProvider.isValidToken(accessToken)) {
                UserDetails userDetails = getUserDetails(accessToken);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (AuthException e) {
            response.setContentType("application/json;charset=UTF-8");
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getErrorCode(), e.getMessage());
            response.setStatus(e.getStatus().value());
            objectMapper.writeValue(response.getWriter(), exceptionResponse);
        }
    }


    /*
    JWT를 이용해 UserDetails 반환 메서드
    - [param] : jwt
    - [return] : UserDetails 객체
     */
    private UserDetails getUserDetails(String accessToken) {
        String username = jwtProvider.getUsername(accessToken);
        return userDetailService.loadUserByUsername(username);
    }

    /*
    Toekn 추출 메서드
    - [param] : HTTP request
    - [return] : JWT
    - 헤더에서 Authorization 부분의 Bearer 부분을 제거후 JWT만을 반환
     */
    private String resolveToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
