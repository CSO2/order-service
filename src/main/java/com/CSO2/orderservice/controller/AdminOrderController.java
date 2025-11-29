package com.CSO2.orderservice.controller;

import com.CSO2.orderservice.service.OrderProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderProcessingService orderProcessingService;

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable String id, @RequestParam String status) {
        orderProcessingService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
