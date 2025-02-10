package com.ll.nbe342team8.domain.member.member.controller;

import com.ll.nbe342team8.domain.jwt.AuthService;
import com.ll.nbe342team8.domain.member.member.dto.MemberDto;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Optional;

@RequestMapping("/api/auth/me")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser securityUser) {
            return ResponseEntity.ok(new MemberDto(securityUser.getMember()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    //마이페이지 데이터를 불러온다. 마이페이지는 resMemberMyPageDto 데이터를 이용해 마이페이지를 구성한다.
    @Operation(summary = "사용자 정보 조회")
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

        /*
        @GetMapping("/my")
        public ResponseEntity<?> getMyPage(@CookieValue(value = "accessToken", required = false) String token) {
            Member member = authService.getMemberFromToken(token);
            // 명시적으로 지연 로딩 데이터 초기화

            ResMemberMyPageDto memberMyPageDto = new ResMemberMyPageDto(member);
            return ResponseEntity.ok(memberMyPageDto);
        }*/

    }

    //사용자 정보를 갱신하는 기능(배송 정보 제외)이며 갱신 정보는 putReqMemberMyPageDto로 받는다.
    @Operation(summary = "사용자 정보 갱신")
    @PutMapping("/my")
    public ResponseEntity<?> putMyPage(@RequestBody @Valid PutReqMemberMyPageDto putReqMemberMyPageDto
                                      ) {
        //@RequestHeader("Authorization") String token 변수로 추가해서 토큰을 받아온다.
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

        /*
           @PutMapping("/profile")
    public ResponseEntity<?> putMyPage(@CookieValue(value = "accessToken", required = false) String token,
                                       @RequestBody @Valid PutReqMemberMyPageDto putReqMemberMyPageDto) {
        Member member = authService.getMemberFromToken(token);

        member = memberService.modifyOrJoin(member.getOauthId(), putReqMemberMyPageDto, member.getEmail());
        // 업데이트된 멤버 정보의 지연 로딩 데이터 초기화
        Hibernate.initialize(member.getDeliveryInformations());

        ResMemberMyPageDto resMemberMyPageDto = new ResMemberMyPageDto(member);
        return ResponseEntity.ok(resMemberMyPageDto);
    }
         */
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return authService.logout();
    }

}
