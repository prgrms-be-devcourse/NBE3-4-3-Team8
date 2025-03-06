package com.ll.nbe342team8.domain.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/order/success")
    public ResponseEntity<?> tossPaymentSuccess(@RequestParam String paymentKey,
                                                @RequestParam String orderId,
                                                @RequestParam long amount,
                                                @RequestParam(required = false) String paymentType) {
        try {
            // PaymentService를 통해 결제 승인 처리
            PaymentResponse paymentResponse = paymentService.confirmPayment(paymentKey, orderId, amount);

            // 결제 타입에 따른 추가 처리 (일반 주문 또는 바로 구매)
            if ("NORMAL".equals(paymentType)) {
                // 일반 주문 처리 로직
                // orderService.completeOrder(orderId);
            } else {
                // 바로 구매 처리 로직
                // orderService.completeFastOrder(orderId);
            }

            // 결제 성공 페이지로 리다이렉트 또는 JSON 응답 반환
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:3000/order/success" +
                                           "?orderId=" + paymentResponse.getOrderId() +
                                           "&amount=" + paymentResponse.getTotalAmount() +
                                           "&paymentKey=" + paymentResponse.getPaymentKey()));

            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (PaymentException e) {
            // 결제 처리 중 오류 발생 시
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage(),
                    "code", "PAYMENT_ERROR"
            ));
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "결제 처리 중 오류가 발생했습니다.",
                    "code", "SERVER_ERROR"
            ));
        }


    }

    // 결제 실패 처리
    @GetMapping("/order/fail")
    public ResponseEntity<?> tossPaymentFail(@RequestParam String code,
                                             @RequestParam String message,
                                             @RequestParam(required = false) String orderId) {
        // 결제 실패 로그 기록
        log.error("결제 실패: code={}, message={}, orderId={}", code, message, orderId);

        // 실패한 주문 상태 업데이트 (필요한 경우)
        if (orderId != null && !orderId.isEmpty()) {
            // orderService.updateOrderStatusToFailed(orderId);
        }

        // 결제 실패 정보 반환
        return ResponseEntity.badRequest().body(Map.of(
                "message", message,
                "code", code
        ));
    }

    // 결제 승인 API 엔드포인트 (프론트엔드 API 라우트에서 호출)
    @PostMapping("/api/payments/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentConfirmRequest request) {
        try {
            PaymentResponse response = paymentService.confirmPayment(
                    request.getPaymentKey(),
                    request.getOrderId(),
                    request.getAmount()
            );
            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage(),
                    "code", "PAYMENT_ERROR"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "결제 처리 중 오류가 발생했습니다.",
                    "code", "SERVER_ERROR"
            ));
        }
    }
}