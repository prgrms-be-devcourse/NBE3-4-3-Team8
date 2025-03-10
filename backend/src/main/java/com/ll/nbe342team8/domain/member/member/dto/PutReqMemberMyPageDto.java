package com.ll.nbe342team8.domain.member.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




public record PutReqMemberMyPageDto(
        @NotBlank(message = "공백은 허용하지 않습니다.")
        @Size(max = 50)
        String name,
        @NotNull
        String phoneNumber
        //@NotNull String profileImageUrl
) {}