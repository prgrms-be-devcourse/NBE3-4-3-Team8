package com.ll.nbe342team8.domain.member.member.service;

import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public void modifyMember(Member member, PutReqMemberMyPageDto dto) {
        //사용자 개체 데이터 갱신
        member.updateMemberInfo(dto);
    }


    public Member getMemberById(Long id){
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
    }

    public Member create(Member member) {
        return memberRepository.save(member);
    }

    public long count(){
        return memberRepository.count();
    }
}
