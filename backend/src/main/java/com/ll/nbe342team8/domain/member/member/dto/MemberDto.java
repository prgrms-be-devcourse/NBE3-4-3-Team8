package com.ll.nbe342team8.domain.member.member.dto;


import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

<<<<<<< HEAD
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
=======
@Getter
@NoArgsConstructor
public class MemberDto {
   private Long id;
   private String oAuthId;
   private String name;
   private String email;
   private Member.MemberType memberType;

   public MemberDto(Member entity) {
       this.id = entity.getId();
       this.oAuthId = entity.getOAuthId();
       this.name = entity.getName();
       this.email = entity.getEmail();
       this.memberType = entity.getMemberType();
   }

   public Map<String, Object> getAttributes() {
       Map<String, Object> attributes = new HashMap<>();
       attributes.put("id", this.id);
       attributes.put("oAuthId", this.oAuthId);
       attributes.put("name", this.name);
       attributes.put("email", this.email);
       attributes.put("memberType", this.memberType);
       return attributes;
   }

   public enum MemberType {
       USER,
       ADMIN
   }
>>>>>>> 5ee7eebc425604cde2cef208c325cb58bbbb69de
}