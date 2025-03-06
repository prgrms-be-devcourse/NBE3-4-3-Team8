package com.ll.nbe342team8.domain.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    @Value("${payment.toss.confirm-url}")
    private String confirmUrl;

    public PaymentResponse confirmPayment(String paymentKey, String orderId, Long amount) {
        try {
            // 토스페이먼츠 시크릿 키를 Base64로 인코딩
            String encodedSecretKey = Base64.getEncoder().encodeToString(
                    (secretKey + ":").getBytes(StandardCharsets.UTF_8)
            );

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + encodedSecretKey);

            // 요청 바디 설정 - HashMap 대신 TossPaymentRequest 객체 사용
            TossPaymentRequest requestBody = new TossPaymentRequest(paymentKey, orderId, amount);

            // HTTP 요청 엔티티 생성
            HttpEntity<TossPaymentRequest> request = new HttpEntity<>(requestBody, headers);

            // 토스페이먼츠 API 호출
            PaymentResponse response = restTemplate.postForObject(
                    confirmUrl,
                    request,
                    PaymentResponse.class
            );

            // 결제 성공 시 비즈니스 로직 처리
            // 예: 주문 상태 업데이트, 결제 정보 저장 등
            processSuccessfulPayment(response);

            return response;
        } catch (RestClientException e) {
            log.error("토스페이먼츠 결제 승인 중 오류 발생: {}", e.getMessage(), e);
            throw new PaymentException("결제 승인 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            log.error("결제 처리 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new PaymentException("결제 처리 중 오류가 발생했습니다.", e);
        }
    }

    private void processSuccessfulPayment(PaymentResponse response) {
        // 결제 성공 후 비즈니스 로직 구현
        // 예: 주문 상태 업데이트, 결제 정보 DB 저장 등
        log.info("결제 성공: orderId={}, amount={}", response.getOrderId(), response.getTotalAmount());

    }

    // 내부 클래스로 요청 객체 정의
    public static class TossPaymentRequest {
        private String paymentKey;
        private String orderId;
        private Long amount;

        public TossPaymentRequest(String paymentKey, String orderId, Long amount) {
            this.paymentKey = paymentKey;
            this.orderId = orderId;
            this.amount = amount;
        }

        public String getPaymentKey() {
            return paymentKey;
        }

        public String getOrderId() {
            return orderId;
        }

        public Long getAmount() {
            return amount;
        }
    }
}