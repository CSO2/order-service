package com.CSO2.orderservice.dto.request;

import com.CSO2.orderservice.entity.ShippingAddress;
import lombok.Data;

@Data
public class CheckoutRequest {
    private String paymentToken;
    private ShippingAddress shippingAddress;
    private String shippingMethod;
}
