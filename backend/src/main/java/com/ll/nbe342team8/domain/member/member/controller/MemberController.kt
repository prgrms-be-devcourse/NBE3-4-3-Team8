package com.ll.nbe342team8.domain.member.member.controller

import com.ll.nbe342team8.domain.book.review.dto.ReviewsResponseDto
import com.ll.nbe342team8.domain.book.review.service.ReviewService
import com.ll.nbe342team8.domain.jwt.AuthService
import com.ll.nbe342team8.domain.member.member.dto.MemberDto
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto
import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.global.exceptions.ServiceException
import com.ll.nbe342team8.standard.PageDto.PageDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@Slf4j
@RequestMapping("/api/auth/me")
@RestController
@Tag(name = "Member", description = "Member API")
@RequiredArgsConstructor
class MemberController (
    private val authService: AuthService,
    private val memberService: MemberService,
    private val reviewService: ReviewService
){


    @GetMapping
    fun getUserInfo(): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication

        val securityUser = authentication?.principal as? SecurityUser
        return if (securityUser != null) {
            ResponseEntity.ok(MemberDto(securityUser.member))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<Void>()
        }
    }


    @GetMapping("/my")
    @Operation(summary = "사용자 정보 조회")
    fun getMyPage(@AuthenticationPrincipal securityUser: SecurityUser?): ResponseEntity<ResMemberMyPageDto>
    //마이페이지 데이터를 불러온다. 마이페이지는 resMemberMyPageDto 데이터를 이용해 마이페이지를 구성한다.
    {
        val member: Member = securityUser?.member?.let { memberService.getMemberById(it.id) }
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        //마이페이지 구성을 위한 데이터 반환
        val memberMyPageDto = ResMemberMyPageDto(member)

        return ResponseEntity.ok(memberMyPageDto)
    }

    //사용자 정보를 갱신하는 기능(배송 정보 제외)이며 갱신 정보는 putReqMemberMyPageDto로 받는다.
    @Operation(summary = "사용자 정보 갱신")
    @PutMapping("/my")
    fun putMyPage(
        @Valid @RequestBody putReqMemberMyPageDto: PutReqMemberMyPageDto,
        @AuthenticationPrincipal securityUser: SecurityUser?
    ): ResponseEntity<ResMemberMyPageDto> {
        val member: Member = securityUser?.member?.let { memberService.getMemberById(it.id) }
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        // jwt 토큰으로 찾은 사용자 개체 갱신
        memberService.modifyOrJoin(member.oAuthId, putReqMemberMyPageDto, member.email)
        val updateMember: Member =memberService.getMemberById(member.id)
        val resMemberMyPageDto = ResMemberMyPageDto(updateMember)

        return ResponseEntity.ok(resMemberMyPageDto)
    }


    @PostMapping("/logout")
    fun logout(): ResponseEntity<*> {
        return authService.logout()
    }

    @GetMapping("/my/reviews")
    @Operation(summary = "사용자 리뷰 조회")
    fun getMemberReviews(@RequestParam(defaultValue = "0") page: Int,
                         @AuthenticationPrincipal securityUser: SecurityUser?): ResponseEntity<PageDto<ReviewsResponseDto>> {
        val member: Member = securityUser?.member?.let { memberService.getMemberById(it.id) }
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        val reviews = reviewService.getMemberReviewPage(member, page)

        return ResponseEntity.ok(reviews)
    }
}
