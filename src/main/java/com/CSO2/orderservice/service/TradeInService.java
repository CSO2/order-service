package com.CSO2.orderservice.service;

import com.CSO2.orderservice.dto.TradeInSubmitRequest;
import com.CSO2.orderservice.entity.TradeInRequest;
import com.CSO2.orderservice.repository.TradeInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TradeInService {

    private final TradeInRepository tradeInRepository;

    public BigDecimal calculateQuote(TradeInSubmitRequest req) {
        // Logic for estimation based on category, specs, and condition
        // This is a simplified logic
        BigDecimal baseValue = new BigDecimal("50.00");
        if ("Good".equalsIgnoreCase(req.getCondition())) {
            baseValue = baseValue.add(new BigDecimal("20.00"));
        }
        return baseValue;
    }

    public void submitRequest(TradeInSubmitRequest req) {
        TradeInRequest tradeInRequest = new TradeInRequest();
        tradeInRequest.setDeviceCategory(req.getCategory());
        tradeInRequest.setDescription(req.getSpecs().toString());
        tradeInRequest.setEstimatedValue(calculateQuote(req));
        tradeInRequest.setStatus(TradeInRequest.TradeInStatus.PENDING);
        // Set userId from context if available
        tradeInRepository.save(tradeInRequest);
    }
}
