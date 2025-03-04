package com.ll.nbe342team8.domain.member.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class PutReqMemberMyPageDto(
        @field:NotBlank(message = "공백은 허용하지 않습니다.")
        @field:Size(max = 50)
        val name: String,

        @field:NotNull
        val phoneNumber: String

        // @field:NotNull val profileImageUrl: String
)
