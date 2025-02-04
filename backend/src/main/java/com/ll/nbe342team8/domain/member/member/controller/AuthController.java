package com.ll.nbe342team8.domain.member.member.controller;

import com.ll.nbe342team8.domain.member.member.dto.KakaoTokenResponse;
import com.ll.nbe342team8.domain.member.member.dto.KakaoUserResponse;
import com.ll.nbe342team8.domain.member.member.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    @GetMapping("/auth/login/kakao")
    public void kakaoLogin(@RequestParam("code") String accessCode) {

        KakaoTokenResponse kakaoTokenResponse=authService.getKakaoToken(accessCode);
        KakaoUserResponse kakaoUserResponse=authService.requestProfile(kakaoTokenResponse);
        authService.createNewUser(kakaoUserResponse);

        System.out.println("------------------------------------------");
        System.out.println("회원가입 성공!");
        System.out.println("------------------------------------------");

    }



}
