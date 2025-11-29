package com.CSO2.orderservice.controller;

import com.CSO2.orderservice.dto.request.CheckoutRequest;
import com.CSO2.orderservice.dto.response.OrderDetailDTO;
import com.CSO2.orderservice.dto.response.OrderSummaryDTO;
import com.CSO2.orderservice.entity.Order;
import com.CSO2.orderservice.service.OrderProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderProcessingService orderProcessingService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestHeader("X-User-Id") String userId,
            @RequestBody CheckoutRequest req) {
        return ResponseEntity.ok(orderProcessingService.createOrder(userId, req));
    }

    @GetMapping
    public ResponseEntity<List<OrderSummaryDTO>> getUserOrders(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(orderProcessingService.getUserOrders(userId));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<OrderSummaryDTO>> getRecentOrders() {
        return ResponseEntity.ok(orderProcessingService.getRecentOrders());
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderSummaryDTO>> getAllOrders() {
        return ResponseEntity.ok(orderProcessingService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> getOrderDetails(@PathVariable String id) {
        return ResponseEntity.ok(orderProcessingService.getOrderDetails(id));
    }
}
