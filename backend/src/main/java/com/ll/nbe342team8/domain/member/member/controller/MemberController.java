package com.ll.nbe342team8.domain.member.member.controller;

import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto;
import com.ll.nbe342team8.domain.book.review.dto.ReviewsResponseDto;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.service.ReviewService;
import com.ll.nbe342team8.domain.book.review.type.ReviewSortType;
import com.ll.nbe342team8.domain.jwt.AuthService;
import com.ll.nbe342team8.domain.member.member.dto.MemberDto;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import com.ll.nbe342team8.standard.PageDto.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@Slf4j
@RequestMapping("/api/auth/me")
@RestController
@Tag(name = "Member", description = "Member API")
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @GetMapping
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        //마이페이지 구성을 위한 데이터 반환
        ResMemberMyPageDto memberMyPageDto=new ResMemberMyPageDto(member);

        return  ResponseEntity.ok(memberMyPageDto);



    }

    //사용자 정보를 갱신하는 기능(배송 정보 제외)이며 갱신 정보는 putReqMemberMyPageDto로 받는다.
    @Operation(summary = "사용자 정보 갱신")
    @PutMapping("/my")
    public ResponseEntity<?> putMyPage(@RequestBody @Valid PutReqMemberMyPageDto putReqMemberMyPageDto
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {

            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        // jwt 토큰으로 찾은 사용자 개체 갱신
        memberService.modifyOrJoin(member.getOAuthId(), putReqMemberMyPageDto, member.getEmail());

        ResMemberMyPageDto resMemberMyPageDto = new ResMemberMyPageDto(member);

        return ResponseEntity.ok(resMemberMyPageDto);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return authService.logout();
    }

    @GetMapping("/my/reviews")
    @Operation(summary = "사용자 리뷰 조회")
    public ResponseEntity<PageDto<ReviewsResponseDto>> getMemberReviews(@RequestParam(defaultValue = "0") int page) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            log.info("리뷰 조회 인증 실패!");
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        PageDto<ReviewsResponseDto> reviews = reviewService.getMemberReviewPage(member,page);

        return ResponseEntity.ok(reviews);
    }



}
