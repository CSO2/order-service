package com.CSO2.orderservice.client;

import com.CSO2.orderservice.dto.request.StockCheckItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-catalogue-service", url = "${catalog.service.url:http://localhost:8082}")
public interface CatalogClient {

    @PostMapping("/api/products/validate-stock")
    List<String> validateStock(@RequestBody List<StockCheckItem> items);

    @PostMapping("/api/products/reduce-stock")
    void reduceStock(@RequestBody List<StockCheckItem> items);
}
