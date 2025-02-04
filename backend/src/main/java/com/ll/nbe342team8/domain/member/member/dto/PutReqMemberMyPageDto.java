package com.ll.nbe342team8.domain.member.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Getter
@Setter
@NoArgsConstructor
public class PutReqMemberMyPageDto {

    @NotBlank(message = "공백은 허용하지 않습니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9 ]+$", message = "이름에는 특수문자를 포함할 수 없습니다.")
    @JsonProperty("name")
    String name;


    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다. (010-XXXX-XXXX)")
    @NotBlank(message = "공백은 허용하지 않습니다.")
    @JsonProperty("phoneNumber")
    String phoneNumber;


}
