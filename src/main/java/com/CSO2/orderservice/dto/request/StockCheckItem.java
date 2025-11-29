package com.CSO2.orderservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockCheckItem {
    private String productId;
    private Integer quantity;
}
