package com.ll.nbe342team8.domain.member.member.service;

import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public Member modifyOrJoin(String oAuthId, PutReqMemberMyPageDto dto, String email) {
        return memberRepository.findByoAuthId(oAuthId) // 기존 회원인지 확인 (oAuthId 기준으로 검색)
                .map(member -> {
                    // 기존 회원 정보 업데이트
                    member.updateMemberInfo(dto);
                    member.setEmail(email); // 이메일 업데이트 추가
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    // 새 회원 생성 시 기본값으로 USER 타입 설정
                    Member member = Member.builder()
                            .oAuthId(oAuthId)
                            .email(email)
                            .name(dto.name())
                            .phoneNumber(dto.phoneNumber() != null ? dto.phoneNumber() : "")//전화번호가 없으면 빈 문자열("") 저장
                            .memberType(Member.MemberType.USER)
                            //.profileImageUrl(dto.profileImageUrl())
                            .password("")
                            .build();
                    return memberRepository.save(member);
                });
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

    public Optional<Member> findByOauthId(String oAuthId) {
        return memberRepository.findByoAuthId(oAuthId);
    }

    //테스트용 메서드
    @Transactional
    public void saveMember(Member member) { memberRepository.save(member);}

    @Override
    public UserDetails loadUserByUsername(String oAuthId) throws UsernameNotFoundException {
        Member member = findByOauthId(oAuthId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (member.getMemberType() != Member.MemberType.ADMIN) {
            throw new UsernameNotFoundException("관리자 권한이 없습니다.");
        }

        return new User(
                member.getOAuthId(),
                "",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
