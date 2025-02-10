package com.ll.nbe342team8.domain.order.detailOrder.service;

import com.ll.nbe342team8.domain.order.detailOrder.dto.DetailOrderDto;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetailOrderService {
    private final DetailOrderRepository detailOrderRepository;

    public DetailOrderService(DetailOrderRepository detailOrderRepository){
        this.detailOrderRepository = detailOrderRepository;
    }

    public List<DetailOrderDto> getDetailOrdersByOrderId(Long orderId) {
        List<DetailOrder> detailOrders = detailOrderRepository.findByOrderId(orderId);
        return detailOrders.stream()
                .map(detailOrder -> new DetailOrderDto(
                        detailOrder.getOrder().getId(),
                        detailOrder.getBook().getId(),
                        detailOrder.getBookQuantity(),
                        detailOrder.getDeliveryStatus()))
                .collect(Collectors.toList());

    }
    }


