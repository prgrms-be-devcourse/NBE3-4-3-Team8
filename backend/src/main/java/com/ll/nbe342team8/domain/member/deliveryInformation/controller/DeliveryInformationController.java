package com.ll.nbe342team8.domain.member.deliveryInformation.controller;

import com.ll.nbe342team8.domain.member.deliveryInformation.dto.ReqDeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.deliveryInformation.service.DeliveryInformationService;
import com.ll.nbe342team8.domain.member.deliveryInformation.dto.DeliveryInformationDto;
import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DeliveryInformationController {

    private final DeliveryInformationService deliveryInformationService;
    private final MemberService memberService;

    // 배송 정보 등록. 5개 까지 등록 할 수 있으며 한번에 하나씩 등록한다.
    // 등록할 배송 정보인 DeliveryInformationDto를 매개변수로 받는다.
    @PostMapping("/my/deliveryInformation")
    public ResponseEntity<?> postDeliveryInformation(@RequestBody @Valid ReqDeliveryInformationDto reqDeliveryInformationDto
            ) {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";

        Optional<Member> optionalMember = memberService.findByEmail(email);

        //이메일에 대응하는 사용자가 없는 경우 에러 발생
        if(optionalMember.isEmpty()) { throw new ServiceException(404,"사용자를 찾을 수 없습니다.");}

        Member member=optionalMember.get();

        //배송 정보 설정은 5개 까지 허용한다. 5개 일때 배송지 추가 등록 요청이 올 경우 에러를 반환한다.
        if(member.getDeliveryInformations().size() >=5) { throw new ServiceException(400,"배송지는 5개까지 설정할수있습니다."); }

        //사용자 개체의 배송 정보 리스트에 배송 정보 추가
        deliveryInformationService.addDeliveryInformation(member,reqDeliveryInformationDto);

        //갱신된 사용자 개체를 dto로 변환해 반환한다. 프론트에선 반환 받는 memberDto로 마이페이지 갱신
        ResMemberMyPageDto resMemberMyPageDto=new ResMemberMyPageDto(member);
        return ResponseEntity.status(200).body(resMemberMyPageDto);
    }

    //등록해논 배송 정보 삭제기능. 하나씩 삭제 가능하며 배송 정보의 id를 매개변수로 받는다.
    @DeleteMapping("/my/deliveryInformation/{id}")
    public ResponseEntity<?> deleteDeliveryInformation(@PathVariable(name = "id") Long id
           ) {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";

        Optional<Member> optionalMember = memberService.findByEmail(email);

        if(optionalMember.isEmpty()) { throw new ServiceException(404,"사용자를 찾을 수 없습니다.");}

        Member member=optionalMember.get();

        //배송 정보 id로 배송 정보를 찾아 삭제
        deliveryInformationService.deleteDeliveryInformation(member,id);

        // dto로 갱신된 member 데이터를 반환
        ResMemberMyPageDto resMemberMyPageDto=new ResMemberMyPageDto(member);
        return ResponseEntity.status(200).body(resMemberMyPageDto);
    }

    //등록해논 배송 정보를 갱신한다. 하나씩 갱신 가능하며 배송 정보 id를 매개변수로 받는다.
    @PutMapping("/my/deliveryInformation/{id}")
    public ResponseEntity<?> putDeliveryInformation(@PathVariable(name = "id") Long id
                                                    , @RequestBody @Valid ReqDeliveryInformationDto reqDeliveryInformationDto) {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";

        Optional<Member> optionalMember = memberService.findByEmail(email);

        if(optionalMember.isEmpty()) { throw new ServiceException(404,"사용자를 찾을 수 없습니다.");}

        Member member=optionalMember.get();

        // 갱신할 배송 정보를 id로 탐색
        Optional<DeliveryInformation> optionalDeliveryInformation = deliveryInformationService.findById(id);

        if(optionalDeliveryInformation.isEmpty()) { throw new ServiceException(404,"배송지를 찾을 수 없습니다.");}

        //배송 정보와 기본 배송지 설정을 갱신한다.
        deliveryInformationService.modifyDeliveryInformation(optionalDeliveryInformation.get(),reqDeliveryInformationDto,member);

        ResMemberMyPageDto resMemberMyPageDto=new ResMemberMyPageDto(member);

        return ResponseEntity.status(200).body(resMemberMyPageDto);
    }


}
