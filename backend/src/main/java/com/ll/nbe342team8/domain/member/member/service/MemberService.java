package com.ll.nbe342team8.domain.member.member.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
