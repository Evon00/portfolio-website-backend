package com.example.portfolio_website_backend.member.controller;

import com.example.portfolio_website_backend.common.dto.SuccessResponse;
import com.example.portfolio_website_backend.member.dto.request.MemberLoginRequestDTO;
import com.example.portfolio_website_backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;


    /*
     - 회원가입의 경우, Admin 계정 만들기용이기에, 사용하는 일이 없을 경우 비활성화
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody MemberRegisterRequestDTO requestDTO){
        return ResponseEntity.ok(SuccessResponse.ok(memberService.register(requestDTO)));
    }
     */

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginRequestDTO requestDTO){
        return ResponseEntity.ok(SuccessResponse.ok(memberService.login(requestDTO)));
    }
}
