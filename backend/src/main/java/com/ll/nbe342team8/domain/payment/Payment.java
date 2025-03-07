package com.ll.nbe342team8.domain.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tossOrderId;             // 토스 주문ID (예: "ORDER_20250306_ABC123")

    private String paymentKey;              // 토스 결제키

    private Long paidAmount;                // 결제금액

    private LocalDateTime paySuccessDate;   // 결제 성공 시각

    private String method;                  // 결제 수단
}