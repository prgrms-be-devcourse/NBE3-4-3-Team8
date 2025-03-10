package com.ll.nbe342team8.domain.member.deliveryInformation.controller

import com.ll.nbe342team8.domain.member.deliveryInformation.dto.ReqDeliveryInformationDto
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation
import com.ll.nbe342team8.domain.member.deliveryInformation.service.DeliveryInformationService
import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.global.exceptions.ServiceException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.Duration


@Tag(name = "DeliveryInformationController", description = "배송 정보 컨트롤러")
@RestController
@RequiredArgsConstructor
class DeliveryInformationController(
    private val deliveryInformationService: DeliveryInformationService,
    private val memberService: MemberService
) {

    // 배송 정보 등록. 5개 까지 등록 할 수 있으며 한번에 하나씩 등록한다.
    // 등록할 배송 정보인 DeliveryInformationDto를 매개변수로 받는다.
    @Operation(summary = "배송 정보 등록(최대 5개)")
    @PostMapping("/my/deliveryInformation")
    fun postDeliveryInformation(
        @RequestBody reqDeliveryInformationDto: @Valid ReqDeliveryInformationDto,
        @AuthenticationPrincipal securityUser: SecurityUser?
    ): ResponseEntity<ResMemberMyPageDto> {
        val member: Member = securityUser?.member?.let { memberService.getMemberById(it.id) }
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        // 단 시간 내 중복 등록 방지
        deliveryInformationService.validateExistsDuplicateDeliveryInformationInShortTime(member,reqDeliveryInformationDto,Duration.ofSeconds(5))

        //배송 정보 설정은 5개 까지 허용한다. 5개 일때 배송지 추가 등록 요청이 올 경우 에러를 반환한다.
        if (member.deliveryInformations.size >= 5) {
            throw ServiceException(HttpStatus.CONFLICT.value(), "배송지는 5개까지 설정할수있습니다.")
        }

        //사용자 개체의 배송 정보 리스트에 배송 정보 추가
        deliveryInformationService.addDeliveryInformation(member, reqDeliveryInformationDto)

        //갱신된 사용자 개체를 dto로 변환해 반환한다. 프론트에선 반환 받는 memberDto로 마이페이지 갱신
        val resMemberMyPageDto = ResMemberMyPageDto(member)
        return ResponseEntity.status(HttpStatus.CREATED).body(resMemberMyPageDto)
    }

    //등록해논 배송 정보 삭제기능. 하나씩 삭제 가능하며 배송 정보의 id를 매개변수로 받는다.
    @Operation(summary = "배송 정보 삭제 (한개)")
    @DeleteMapping("/my/deliveryInformation/{id}")
    fun deleteDeliveryInformation(
        @PathVariable id: Long,
        @AuthenticationPrincipal securityUser: SecurityUser?
    ): ResponseEntity<ResMemberMyPageDto> {
        val member: Member = securityUser?.member?.let { memberService.getMemberById(it.id) }
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        // 삭제할 배송 정보를 id로 탐색
        val deliveryInformation = deliveryInformationService.findById(id)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "배송정보를 찾을 수 없습니다.") }!!

        deliveryInformationService.validateDeliveryInformationOwner(member, deliveryInformation)

        //배송 정보 id로 배송 정보를 찾아 삭제
        deliveryInformationService.deleteDeliveryInformation(deliveryInformation, member)

        // dto로 갱신된 memberId 데이터를 반환
        val resMemberMyPageDto = ResMemberMyPageDto(member)
        return ResponseEntity.status(HttpStatus.OK).body(resMemberMyPageDto)
    }

    //등록해논 배송 정보를 갱신한다. 하나씩 갱신 가능하며 배송 정보 id를 매개변수로 받는다.
    @Operation(summary = "배송 정보 갱신 (한개)")
    @PutMapping("/my/deliveryInformation/{id}")
    fun putDeliveryInformation(
        @PathVariable id: Long,
        @RequestBody reqDeliveryInformationDto: @Valid ReqDeliveryInformationDto,
        @AuthenticationPrincipal securityUser: SecurityUser?
    ): ResponseEntity<ResMemberMyPageDto> {
        val member: Member = securityUser?.member?.let { memberService.getMemberById(it.id) }
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        // 갱신할 배송 정보를 id로 탐색
        val deliveryInformation = deliveryInformationService.findById(id)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "배송 정보를 찾을 수 없습니다.") }!!

        deliveryInformationService.validateDeliveryInformationOwner(member, deliveryInformation)

        //validateExistsDuplicateQuestionInShortTime 추가 필요(매개변수 너무 많은거 해결 필요)

        //배송 정보와 기본 배송지 설정을 갱신한다.
        deliveryInformationService.modifyDeliveryInformation(deliveryInformation, reqDeliveryInformationDto, member)

        val resMemberMyPageDto = ResMemberMyPageDto(member)

        return ResponseEntity.ok(resMemberMyPageDto)
    }







}