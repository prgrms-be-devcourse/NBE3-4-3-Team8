package com.ll.nbe342team8.domain.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;

    @Value("${payment.toss.secret-key}")
    private String secretKey;
    @Value("${payment.toss.confirm-url}")
    private String confirmUrl;

    @Transactional
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
            // 요청 바디 설정 - TossPaymentRequest 객체 사용
            TossPaymentRequest requestBody = new TossPaymentRequest(paymentKey, orderId, amount);
            // HTTP 요청 엔티티 생성
            HttpEntity<TossPaymentRequest> request = new HttpEntity<>(requestBody, headers);
            // 토스페이먼츠 API 호출
            PaymentResponse response = restTemplate.postForObject(
                    confirmUrl,
                    request,
                    PaymentResponse.class
            );
            // 결제 성공 시 주문 상태 업데이트 및 결제 정보 저장
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
        // 결제 성공 후 주문(Order)과 관련된 정보를 업데이트하거나 저장하는 로직 추가
        log.info("결제 성공: orderId={}, amount={}", response.getOrderId(), response.getTotalAmount());
        log.info("response: {}", response);

        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(response.getApprovedAt(), isoFormatter);

        paymentRepository.save(Payment.builder()
                .tossOrderId(response.getOrderId())
                .paymentKey(response.getPaymentKey())
                .paidAmount(response.getTotalAmount())
                .paySuccessDate(offsetDateTime.toLocalDateTime())
                .method(response.getMethod())
                .build());

        // OrderService를 통해 주문 내역, 상세 주문 내역 저장하는 로직 추가
    }

    // 내부 클래스로 요청 객체 정의
    public record TossPaymentRequest(
            String paymentKey,
            String orderId,
            Long amount) {
    }
}