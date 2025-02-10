package com.ll.nbe342team8.domain.member.member.dto;


import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

public record MemberDto(
        String oauthId,
        String name,
        String email,
        Member.MemberType memberType
) {
    public MemberDto(Member member) {
        this(member.getOauthId(), member.getName(), member.getEmail(), member.getMemberType());
    }

    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("oauthId", oauthId);
        attributes.put("name", name);
        attributes.put("email", email);
        attributes.put("memberType", memberType);
        return attributes;
    }
}