package com.ll.nbe342team8.domain.order.detailOrder.dto;

import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDetailOrderStatusRequest {

	@Schema(description = "변경할 배송 상태")
	private DeliveryStatus status;
}
