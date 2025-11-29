package com.CSO2.orderservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private String productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
    private BigDecimal subtotal;
}
