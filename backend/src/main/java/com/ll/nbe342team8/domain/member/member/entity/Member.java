package com.ll.nbe342team8.domain.member.member.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	private Long id;

	private String name; // 사용자 이름

	private String phoneNumber; // 전화번호

	@Enumerated(EnumType.STRING)
	private MemberType memberType; // 사용자 역할(사용자, 관리자)

	private String oAuthId; // 필드 이름 변경

	private String email; // 사용자 이메일

	private String password;

	private String username;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DeliveryInformation> deliveryInformations;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	private List<Cart> carts;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Question> questions;

  private String profileImageUrl;

  // Enum 사용자 역할
  public enum MemberType {
      USER,
      ADMIN
  }
  
 	public void updateMemberInfo(PutReqMemberMyPageDto dto) {
		this.name = dto.name();
		this.phoneNumber = dto.phoneNumber();
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
}
