package com.ll.nbe342team8.domain.member.member.controller;

import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Optional;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    //마이페이지 데이터를 불러온다. 마이페이지는 resMemberMyPageDto 데이터를 이용해 마이페이지를 구성한다.
    @GetMapping("/my")
    public ResponseEntity<?> getMyPage() {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";

        Optional<Member> optionalMember = memberService.findByEmail(email);

        //사용자가 존재하지 않는 경우 에러 반환
        if(optionalMember.isEmpty()) { throw new ServiceException(404,"사용자를 찾을 수 없습니다.");}

        //마이페이지 구성을 위한 데이터 반환
        ResMemberMyPageDto memberMyPageDto=new ResMemberMyPageDto(optionalMember.get());

        return ResponseEntity.status(200).body(memberMyPageDto);

    }

    //사용자 정보를 갱신하는 기능(배송 정보 제외)이며 갱신 정보는 putReqMemberMyPageDto로 받는다.
    @PutMapping("/my")
    public ResponseEntity<?> putMyPage(@RequestBody @Valid PutReqMemberMyPageDto putReqMemberMyPageDto
                                      ) {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";
        //modifyOrJoin()`이 `String oauthId`를 요구하므로
        // `member.getOauthId()`와 `member.getEmail()`을 인자로 전달하도록 수정
        Optional<Member> optionalMember = memberService.findByEmail(email);

        if(optionalMember.isEmpty()) { throw new ServiceException(404,"사용자를 찾을 수 없습니다.");}

        Member member=optionalMember.get();

        // jwt 토큰으로 찾은 사용자 개체 갱신
        memberService.modifyOrJoin(member.getOauthId(), putReqMemberMyPageDto, member.getEmail());

        ResMemberMyPageDto resMemberMyPageDto=new ResMemberMyPageDto(member);

        return ResponseEntity.status(200).body(resMemberMyPageDto);
    }
    //아래 코드는 jwt토큰을 사용하지 않고 OAuth2 기반으로 사용자 정보를 갱신하는 코드

  /*
     @GetMapping("/me")
    public ResponseEntity<ResMemberMyPageDto> getCurrentUser(@AuthenticationPrincipal SecurityUser principal) {
        if (principal == null) {
            return ResponseEntity.ok(null);
        }

        try {
            String oauthId = principal.getName(); // OAuth2 provider의 user-name-attribute 값
            Member member = memberService.findByOauthId(oauthId)
                    .orElseThrow(() -> new ServiceException(404, "사용자를 찾을 수 없습니다."));
            return ResponseEntity.ok(new ResMemberMyPageDto(member));
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }
    @GetMapping("/my")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal SecurityUser principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Optional<Member> optionalMember = memberService.findByEmail(principal.getEmail());

        if (optionalMember.isEmpty()) {
            throw new ServiceException(404, "사용자를 찾을 수 없습니다.");
        }

        ResMemberMyPageDto memberMyPageDto = new ResMemberMyPageDto(optionalMember.get());
        return ResponseEntity.status(200).body(memberMyPageDto);
    }


    @PutMapping("/my")
    public ResponseEntity<?> putMyPage(@AuthenticationPrincipal SecurityUser principal,
                                       @RequestBody @Valid PutReqMemberMyPageDto putReqMemberMyPageDto) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Optional<Member> optionalMember = memberService.findByEmail(principal.getEmail());

        if (optionalMember.isEmpty()) {
            throw new ServiceException(404, "사용자를 찾을 수 없습니다.");
        }

        Member member = optionalMember.get();

        memberService.modifyOrJoin(member.getOauthId(), putReqMemberMyPageDto, member.getEmail());

        ResMemberMyPageDto resMemberMyPageDto = new ResMemberMyPageDto(member);
        return ResponseEntity.status(200).body(resMemberMyPageDto);
    }*/

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        //현재 사용자의 세션을 무효화
        request.getSession().invalidate();

        // 쿠키 삭제 (JSESSIONID가 있다면 삭제)
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("로그아웃 완료");
    }
}
