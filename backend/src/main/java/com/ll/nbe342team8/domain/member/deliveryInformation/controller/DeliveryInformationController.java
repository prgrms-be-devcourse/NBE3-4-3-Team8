package com.ll.nbe342team8.domain.member.deliveryInformation.controller;

import com.ll.nbe342team8.domain.member.deliveryInformation.dto.ReqDeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.deliveryInformation.service.DeliveryInformationService;
import com.ll.nbe342team8.domain.member.deliveryInformation.dto.DeliveryInformationDto;
import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Tag(name = "DeliveryInformationController", description = "배송 정보 컨트롤러")
@RestController
@RequiredArgsConstructor
public class DeliveryInformationController {

    private final DeliveryInformationService deliveryInformationService;
    private final MemberService memberService;

    // 배송 정보 등록. 5개 까지 등록 할 수 있으며 한번에 하나씩 등록한다.
    // 등록할 배송 정보인 DeliveryInformationDto를 매개변수로 받는다.
    @Operation(summary = "배송 정보 등록(최대 5개)")
    @PostMapping("/my/deliveryInformation")
    public ResponseEntity<?> postDeliveryInformation(@RequestBody @Valid ReqDeliveryInformationDto reqDeliveryInformationDto
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        //배송 정보 설정은 5개 까지 허용한다. 5개 일때 배송지 추가 등록 요청이 올 경우 에러를 반환한다.
        if(member.getDeliveryInformations().size() >=5) { throw new ServiceException(HttpStatus.CONFLICT.value(), "배송지는 5개까지 설정할수있습니다."); }

        //사용자 개체의 배송 정보 리스트에 배송 정보 추가
        deliveryInformationService.addDeliveryInformation(member,reqDeliveryInformationDto);

        //갱신된 사용자 개체를 dto로 변환해 반환한다. 프론트에선 반환 받는 memberDto로 마이페이지 갱신
        ResMemberMyPageDto resMemberMyPageDto=new ResMemberMyPageDto(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(resMemberMyPageDto);
    }

    //등록해논 배송 정보 삭제기능. 하나씩 삭제 가능하며 배송 정보의 id를 매개변수로 받는다.
    @Operation(summary = "배송 정보 삭제 (한개)")
    @DeleteMapping("/my/deliveryInformation/{id}")
    public ResponseEntity<?> deleteDeliveryInformation(@PathVariable(name = "id") Long id
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        // 삭제할 배송 정보를 id로 탐색
        DeliveryInformation deliveryInformation = deliveryInformationService.findById(id)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "배송정보를 찾을 수 없습니다."));

        validateDeliveryInformationOwner(member, deliveryInformation);

        //배송 정보 id로 배송 정보를 찾아 삭제
        deliveryInformationService.deleteDeliveryInformation(deliveryInformation,member);

        // dto로 갱신된 memberId 데이터를 반환
        ResMemberMyPageDto resMemberMyPageDto=new ResMemberMyPageDto(member);
        return ResponseEntity.status(HttpStatus.OK).body(resMemberMyPageDto);
    }

    //등록해논 배송 정보를 갱신한다. 하나씩 갱신 가능하며 배송 정보 id를 매개변수로 받는다.
    @Operation(summary = "배송 정보 갱신 (한개)")
    @PutMapping("/my/deliveryInformation/{id}")
    public ResponseEntity<?> putDeliveryInformation(@PathVariable(name = "id") Long id
            , @RequestBody @Valid ReqDeliveryInformationDto reqDeliveryInformationDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        // 갱신할 배송 정보를 id로 탐색
        DeliveryInformation deliveryInformation = deliveryInformationService.findById(id)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "배송 정보를 찾을 수 없습니다."));

        validateDeliveryInformationOwner(member, deliveryInformation);
        //validateExistsDuplicateQuestionInShortTime 추가 필요(매개변수 너무 많은거 해결 필요)

        //배송 정보와 기본 배송지 설정을 갱신한다.
        deliveryInformationService.modifyDeliveryInformation(deliveryInformation,reqDeliveryInformationDto,member);

        ResMemberMyPageDto resMemberMyPageDto=new ResMemberMyPageDto(member);

        return ResponseEntity.ok(resMemberMyPageDto);
    }

    //사용자 권한 확인, 관리자 계정이여도 접근 가능
    private void validateDeliveryInformationOwner(Member member, DeliveryInformation deliveryInformation) {
        if (!(deliveryInformationService.isDeliveryInformationOwner(member, deliveryInformation) || checkAdmin(member))) {
            throw new ServiceException(HttpStatus.FORBIDDEN.value(), "권한이 없습니다.");
        }
    }



    private boolean checkAdmin(Member member) {
        return member.getMemberType() == Member.MemberType.ADMIN;
    }


}