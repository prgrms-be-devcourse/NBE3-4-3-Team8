package com.ll.nbe342team8.domain.member.member.dto

import com.ll.nbe342team8.domain.member.member.entity.Member
import lombok.Getter
import lombok.NoArgsConstructor


@Getter
@NoArgsConstructor
class MemberDto(entity: Member) {
    private val id: Long = entity.id
    private val oAuthId: String = entity.oAuthId
    private val name: String? = entity.name
    private val email: String = entity.email
    private val memberType: Member.MemberType = entity.memberType
    private val profileImageUrl: String = entity.profileImageUrl

    val attributes: Map<String, Any?>
        get() {
            val attributes: MutableMap<String, Any?> = HashMap()
            attributes["id"] = id
            attributes["oAuthId"] = oAuthId
            attributes["name"] = name
            attributes["email"] = email
            attributes["memberType"] = memberType
            attributes["profileImageUrl"] = profileImageUrl
            return attributes
        }

    enum class MemberType {
        USER,
        ADMIN
    }
}