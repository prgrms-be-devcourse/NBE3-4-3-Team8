package com.ll.nbe342team8.domain.member.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class PutReqMemberMyPageDto(

        @field:Size(max = 50)
        val name: String,

        val phoneNumber: String

        //val profileImageUrl: String
)
