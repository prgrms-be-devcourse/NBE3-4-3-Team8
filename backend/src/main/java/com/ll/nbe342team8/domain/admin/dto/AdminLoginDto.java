package com.ll.nbe342team8.domain.admin.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginDto {
    private String username;

    @Column(nullable = false)
    private String password;
}