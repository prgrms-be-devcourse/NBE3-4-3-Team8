package com.ll.nbe342team8.domain.member.member.repository

import com.ll.nbe342team8.domain.member.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Optional<Member>

    fun findByoAuthId(oAuthId: String): Optional<Member>

    // 관리자 계정 조회 (이메일ID, 회원 유형 확인)
    fun findByEmailAndMemberType(email: String, memberType: Member.MemberType): Optional<Member>

    // 관리자 계정 개수 확인
    fun countByMemberType(memberType: Member.MemberType): Long
}
