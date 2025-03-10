package com.ll.nbe342team8.global.globalExceptionHandler;

import com.ll.nbe342team8.domain.payment.PaymentException;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // Todo: 빈 리스트에서 getFirst 를 호출해도 NoSuchElementException이 발생함. 예외를 이렇게 받는건 신뢰할 수 없는 결과를 낼 것으로 예상됨.

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, String>> handlePaymentException(PaymentException e) {
        log.error("결제 처리 중 오류 발생: {}", e.getMessage(), e);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", e.getMessage());
        errorResponse.put("code", "PAYMENT_ERROR");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handle(NoSuchElementException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "유효성 검사 에러."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "유효성 검사 에러."));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handle(ServiceException ex) {

        return ResponseEntity
                .status(ex.getResultCode())
                .body(Map.of("message", ex.getMsg()));
    }
}