package com.CSO2.orderservice.dto.response;

import com.CSO2.orderservice.entity.OrderItem;
import com.CSO2.orderservice.entity.ShippingAddress;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OrderDetailDTO {
    private String id;
    private List<OrderItem> items;
    private ShippingAddress address;
    private Map<String, String> timeline; // status -> timestamp
}
