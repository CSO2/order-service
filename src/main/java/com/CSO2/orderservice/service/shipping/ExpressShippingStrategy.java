package com.CSO2.orderservice.service.shipping;

import com.CSO2.orderservice.entity.Order;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component("EXPRESS")
public class ExpressShippingStrategy implements ShippingStrategy {

    @Override
    public BigDecimal calculateCost(Order order) {
        // A higher flat rate for express shipping
        return new BigDecimal("25.00");
    }
}
