package com.example.portfolio_website_backend.common.security.jwt;

import com.example.portfolio_website_backend.common.security.dto.JwtMemberInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;


/*
JwtProvider
- JWT를 생성 및 검증 하는 기능
 */
@Component
public class JwtProvider {

    private final Key key;
    private final Long accessExpirationTime;

    public JwtProvider(
            @Value("${jwt.secret}") String key,
            @Value("${jwt.expirationTime}") Long accessExpirationTime
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessExpirationTime = accessExpirationTime;
    }

    /*
    Access Token 생성
     - [param] JwtMemberInfo : JWT에 담을 사용자 정보
     - [return] Access Token String
     */
    public String createAccessToken(JwtMemberInfo memberInfo) {
        return createToken(memberInfo, accessExpirationTime);
    }

    /*
    JWT 생성
    - [param] JwtMemberInfo : JWT에 담을 사용자 정보 , accessExpirationTime : 토큰 유효기간 (s)
    - [return] JWT String
     */
    private String createToken(JwtMemberInfo memberInfo, long accessExpirationTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberInfo.memberId());
        claims.put("username", memberInfo.username());
        claims.put("name", memberInfo.name());
        claims.put("role", memberInfo.role());

        Instant now = Instant.now();
        Instant tokenValidity = now.plusMillis(accessExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(tokenValidity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /*
    JWT 검증
    - [param] String token
    - [return] IsValidate
     */
    public boolean isValidToken(String token) {

        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            // 예외 처리
        }
        return false;
    }

    /*
    JWT Claims 추출
    - [param] accessToken
    - [return] JWT Claims
     */

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            //유효기간 만료
            return e.getClaims();
        } //오류 코드 추가
    }


    /*
    JWT Claims 에서 사용자 아이디 추출
    - [param] accessToken
    - [return] username
     */
    public String getUsername(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.get("username").toString();
    }
}
