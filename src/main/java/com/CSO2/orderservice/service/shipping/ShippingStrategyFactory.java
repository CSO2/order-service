package com.CSO2.orderservice.service.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
public class ShippingStrategyFactory {

    private final Map<String, ShippingStrategy> strategyMap;

    @Autowired
    public ShippingStrategyFactory(Map<String, ShippingStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public ShippingStrategy getStrategy(String shippingMethod) {
        return Optional.ofNullable(strategyMap.get(shippingMethod))
                .orElseThrow(() -> new IllegalArgumentException("Unknown shipping method: " + shippingMethod));
    }
}
