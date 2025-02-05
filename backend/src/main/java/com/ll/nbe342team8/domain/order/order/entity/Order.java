
package com.ll.nbe342team8.domain.order.order.entity;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseTime {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "total_price")
    private long totalPrice;

    public enum OrderStatus{
        ORDERED,//주문됨
        CANCELLED,//주문 취소됨
        COMPLETE //주문 완료됨(배송까지)
    }

}