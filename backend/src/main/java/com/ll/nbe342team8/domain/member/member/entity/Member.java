package com.ll.nbe342team8.domain.member.member.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTime implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	public Long id;

	public String name; // 사용자 이름

	public String phoneNumber; // 전화번호

	@Enumerated(EnumType.STRING)
	public MemberType memberType; // 사용자 역할(사용자, 관리자)

	public String oAuthId; // 필드 이름 변경

	public String email; // 사용자 이메일

	public String password;

	public String username;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DeliveryInformation> deliveryInformations;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	public List<Cart> carts;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	public List<Question> questions = new ArrayList<>();;

  	public String profileImageUrl;

  // Enum 사용자 역할
  public enum MemberType {
      USER,
      ADMIN
  }

	@PrePersist
	public void prePersist() {
		if (this.profileImageUrl == null || this.profileImageUrl.isEmpty()) {
			this.profileImageUrl = "defaultUrl";
		}
	}
  
 	public void updateMemberInfo(PutReqMemberMyPageDto dto) {
		this.name = dto.getName();
		this.phoneNumber = dto.getPhoneNumber();
	}

	public void addDeliveryInformation(DeliveryInformation deliveryInformation) {
		this.deliveryInformations.add(deliveryInformation);
	}

	public void convertFalseDeliveryInformationsIsDefaultAddress() {
		deliveryInformations.forEach(info -> info.setIsDefaultAddress(false));
	}

	public String getUsername() {
		return oAuthId;
	}

	public String getOAuthId() {
		return oAuthId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (this.memberType == MemberType.ADMIN) {
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else {
			// 일반 사용자라면 필요에 따라 ROLE_USER 또는 빈 리스트를 반환할 수 있음.
			return List.of(new SimpleGrantedAuthority("ROLE_USER"));
		}
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private Member(String oAuthId, String email, String name, String phoneNumber, MemberType memberType, String password) {
		this.oAuthId = oAuthId;
		this.email = email;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.memberType = memberType;
		this.password = password;
	}

	public static Member of(String oAuthId, String email, PutReqMemberMyPageDto dto) {
		return new Member(
				oAuthId,
				email,
				dto.getName(),
				dto.getPhoneNumber() != null ? dto.getPhoneNumber() : "", // 전화번호가 없으면 빈 문자열 저장
				MemberType.USER,
				""
		);
	}

	public boolean checkAdmin() {
		return this.memberType == Member.MemberType.ADMIN;
	}

	public void addQuestion(Question question) {
		this.questions.add(question);
		question.setMember(this);
	}

}
