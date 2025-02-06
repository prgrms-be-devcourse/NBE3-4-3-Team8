package com.ll.nbe342team8.domain.member.member.controller;

import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.Optional;

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

        Optional<Member> optionalMember = memberService.findByEmail(email);

        if(optionalMember.isEmpty()) { throw new ServiceException(404,"사용자를 찾을 수 없습니다.");}

        Member member=optionalMember.get();

        // jwt 토큰으로 찾은 사용자 개체 갱신
        memberService.modifyMember(member,putReqMemberMyPageDto);

        ResMemberMyPageDto resMemberMyPageDto=new ResMemberMyPageDto(member);

        return ResponseEntity.status(200).body(resMemberMyPageDto);
    }





}
