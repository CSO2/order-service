package com.CSO2.orderservice.service.shipping;

import com.CSO2.orderservice.entity.Order;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component("FLAT_RATE")
public class FlatRateShippingStrategy implements ShippingStrategy {

    @Override
    public BigDecimal calculateCost(Order order) {
        return new BigDecimal("10.00"); // The original flat rate
    }
}
