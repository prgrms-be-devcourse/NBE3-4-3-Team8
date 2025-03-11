package com.ll.nbe342team8.domain.member.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.nbe342team8.domain.member.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Optional<Member> findByoAuthId(String oAuthId);

	// 관리자 계정 조회 (이메일ID, 회원 유형 확인)
	Optional<Member> findByEmailAndMemberType(String email, Member.MemberType memberType);

	// 관리자 계정 개수 확인
	long countByMemberType(Member.MemberType memberType);
}
