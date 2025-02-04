package com.ll.nbe342team8.domain.order.detailOrder.controller;

import com.ll.nbe342team8.domain.order.detailOrder.dto.DetailOrderDto;
import com.ll.nbe342team8.domain.order.detailOrder.service.DetailOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/my/orders")
public class DetailOrderController {
    private final DetailOrderService detailOrderService;

    public DetailOrderController(DetailOrderService detailOrderService){
        this.detailOrderService = detailOrderService;
    }
    @GetMapping("/{orderId}/details")
    public ResponseEntity<List<DetailOrderDto>> getDetailOrders(@PathVariable Long orderId){
        List<DetailOrderDto> detailOrders = detailOrderService.getDetailOrdersByOrderId(orderId);
        return ResponseEntity.ok(detailOrders);
    }
}
