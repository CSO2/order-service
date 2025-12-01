package com.CSO2.orderservice.service;

import com.CSO2.orderservice.dto.request.TradeInSubmitRequest;
import com.CSO2.orderservice.entity.TradeInRequest;
import com.CSO2.orderservice.repository.TradeInRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TradeInServiceTest {

    @Mock
    private TradeInRepository tradeInRepository;

    @InjectMocks
    private TradeInService tradeInService;

    @Test
    void calculateQuote_GoodCondition() {
        TradeInSubmitRequest req = new TradeInSubmitRequest();
        req.setCondition("Good");

        BigDecimal quote = tradeInService.calculateQuote(req);

        assertEquals(new BigDecimal("70.00"), quote);
    }

    @Test
    void calculateQuote_OtherCondition() {
        TradeInSubmitRequest req = new TradeInSubmitRequest();
        req.setCondition("Average");

        BigDecimal quote = tradeInService.calculateQuote(req);

        assertEquals(new BigDecimal("50.00"), quote);
    }

    @Test
    void submitRequest() {
        TradeInSubmitRequest req = new TradeInSubmitRequest();
        req.setCategory("Phone");
        req.setCondition("Good");
        req.setSpecs(Map.of("model", "iPhone 12"));

        tradeInService.submitRequest(req);

        verify(tradeInRepository).save(any(TradeInRequest.class));
    }
}
