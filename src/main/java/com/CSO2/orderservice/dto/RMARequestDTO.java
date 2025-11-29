package com.CSO2.orderservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class RMARequestDTO {
    private String orderId;
    private List<String> productIds;
    private String reason;
    private List<String> images;
}
