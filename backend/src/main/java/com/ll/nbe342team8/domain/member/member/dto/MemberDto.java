package com.ll.nbe342team8.domain.member.member.dto;


import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class MemberDto {
   private String oauthId;
   private String name;
   private String email;
   private Member.MemberType memberType;

   public MemberDto(Member entity) {
       this.oauthId = entity.getOauthId();
       this.name = entity.getName();
       this.email = entity.getEmail();
       this.memberType = entity.getMemberType();
   }

   public Map<String, Object> getAttributes() {
       Map<String, Object> attributes = new HashMap<>();
       attributes.put("oauthId", this.oauthId);
       attributes.put("name", this.name);
       attributes.put("email", this.email);
       attributes.put("memberType", this.memberType);
       return attributes;
   }

   public enum MemberType {
       USER,
       ADMIN
   }
}