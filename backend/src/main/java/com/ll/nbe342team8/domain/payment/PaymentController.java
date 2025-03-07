package com.ll.nbe342team8.domain.payment;

import com.ll.nbe342team8.domain.cart.service.CartService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
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
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/order/success")
    public ResponseEntity<?> tossPaymentSuccess(@RequestParam String paymentKey,
                                                @RequestParam String orderId,
                                                @RequestParam long amount) {

        Member member = orderService.getOrderById(orderId).getMember();
        PaymentResponse paymentResponse = paymentService.confirmPayment(paymentKey, orderId, amount);

        if(orderId.startsWith("CART")){
            cartService.deleteProduct(member);
        }

        // 결제 성공 페이지로 리다이렉트 또는 JSON 응답 반환
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:3000/order/success" +
                                       "?orderId=" + paymentResponse.getOrderId() +
                                       "&amount=" + paymentResponse.getTotalAmount() +
                                       "&paymentKey=" + paymentResponse.getPaymentKey()));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
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