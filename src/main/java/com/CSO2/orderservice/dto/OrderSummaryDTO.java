package com.CSO2.orderservice.dto;

import com.CSO2.orderservice.entity.Order.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderSummaryDTO {
    private String id;
    private String userId;
    private BigDecimal total;
    private OrderStatus status;
    private LocalDateTime date;
}
