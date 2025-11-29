package com.CSO2.orderservice.service.shipping;

import com.CSO2.orderservice.entity.Order;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component("FREE")
public class FreeShippingStrategy implements ShippingStrategy {

    @Override
    public BigDecimal calculateCost(Order order) {
        // Free shipping for orders over $100
        if (order.getTotalAmount().compareTo(new BigDecimal("100")) > 0) {
            return BigDecimal.ZERO;
        }
        // Otherwise, fall back to a default rate (or you could throw an exception)
        // For now, let's fall back to the flat rate for simplicity.
        return new BigDecimal("10.00");
    }
}
