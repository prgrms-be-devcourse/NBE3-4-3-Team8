package com.ll.nbe342team8.domain.member.member.dto;


import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class MemberDto {
   private Long id;
   private String oAuthId;
   private String name;
   private String email;
   private Member.MemberType memberType;
   private String profileImageUrl;

   public MemberDto(Member entity) {
       this.id = entity.getId();
       this.oAuthId = entity.getOAuthId();
       this.name = entity.getName();
       this.email = entity.getEmail();
       this.memberType = entity.getMemberType();
       this.profileImageUrl = entity.getProfileImageUrl();
   }

   public Map<String, Object> getAttributes() {
       Map<String, Object> attributes = new HashMap<>();
       attributes.put("id", this.id);
       attributes.put("oAuthId", this.oAuthId);
       attributes.put("name", this.name);
       attributes.put("email", this.email);
       attributes.put("memberType", this.memberType);
       attributes.put("profileImageUrl", this.profileImageUrl);
       return attributes;
   }

   public enum MemberType {
       USER,
       ADMIN
   }
}