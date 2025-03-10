package com.ll.nbe342team8.domain.member.member.service

import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository
import com.ll.nbe342team8.global.exceptions.ServiceException
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.List

@Service
@RequiredArgsConstructor
class MemberService (
    private val memberRepository: MemberRepository ) : UserDetailsService
{


    fun findByEmail(email: String): Optional<Member> {
        return memberRepository.findByEmail(email)
    }

    @Transactional
    fun modifyOrJoin(oAuthId: String, dto: PutReqMemberMyPageDto, email: String): Member {
        return memberRepository.findByoAuthId(oAuthId) // 기존 회원인지 확인 (oAuthId 기준으로 검색)
            .map { member: Member ->
                // 기존 회원 정보 업데이트
                member.updateMemberInfo(dto)
                member.email=email
                memberRepository.save(member)
            }
            .orElseGet {
                // 새 회원 생성 시 기본값으로 USER 타입 설정
                val member = Member.of(oAuthId, email, dto)
                memberRepository.save(member)
            }
    }


    fun getMemberById(id: Long): Member {
        return memberRepository.findById(id).orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다.") }
    }

    fun create(member: Member): Member {
        return memberRepository.save(member)
    }

    fun count(): Long {
        return memberRepository.count()
    }

    fun findByOauthId(oAuthId: String): Optional<Member> {
        return memberRepository.findByoAuthId(oAuthId)
    }

    //테스트용 메서드
    @Transactional
    fun saveMember(member: Member) {
        memberRepository.save(member)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(oAuthId: String): UserDetails {
        val member = findByOauthId(oAuthId)
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다.") }

        if (member.memberType != Member.MemberType.ADMIN) {
            throw UsernameNotFoundException("관리자 권한이 없습니다.")
        }

        return User(
            member.oAuthId,
            "",
            List.of(SimpleGrantedAuthority("ROLE_ADMIN"))
        )
    }
}
