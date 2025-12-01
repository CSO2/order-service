package com.CSO2.orderservice.client;

import com.CSO2.orderservice.dto.response.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "shoppingcart-wishlist-service", url = "${cart.service.url:http://localhost:8084}")
public interface CartClient {

    @GetMapping("/api/cart")
    CartDTO getCart(@RequestHeader("X-User-Id") String userId);

    @DeleteMapping("/api/cart")
    void clearCart(@RequestHeader("X-User-Id") String userId);
}
