package com.ll.nbe342team8.domain.oauth;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;


@Getter
public class SecurityUser extends User implements OAuth2User {
    private final long id;
    private final String nickname;
    private final String email;
    private final Member member;

    public SecurityUser(Member member) {
        super(
                member.getUsername(),  // oAuthId를 username으로 사용
                "",                   // 비밀번호는 빈 문자열
                member.getAuthorities() // Member에서 정의한 권한 사용
        );
        this.id = member.getId();
        this.nickname = member.getName();
        this.email = member.getEmail(); // email 필드명 주의
        this.member = member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "id", id,
                "nickname", nickname,
                "email", email,
                "memberType", member.getMemberType()
        );
    }

    @Override
    public String getName() {
        return getUsername();
    }
}