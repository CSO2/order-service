package com.CSO2.orderservice.controller;

import com.CSO2.orderservice.dto.request.TradeInSubmitRequest;
import com.CSO2.orderservice.service.TradeInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/trade-in")
@RequiredArgsConstructor
public class TradeInController {

    private final TradeInService tradeInService;

    @PostMapping("/quote")
    public ResponseEntity<BigDecimal> calculateQuote(@RequestBody TradeInSubmitRequest req) {
        return ResponseEntity.ok(tradeInService.calculateQuote(req));
    }
}
