package com.CSO2.orderservice.service.shipping;

import com.CSO2.orderservice.entity.Order;
import java.math.BigDecimal;

public interface ShippingStrategy {
    BigDecimal calculateCost(Order order);
}
