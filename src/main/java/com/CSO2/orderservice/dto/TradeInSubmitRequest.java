package com.CSO2.orderservice.dto;

import lombok.Data;
import java.util.Map;

@Data
public class TradeInSubmitRequest {
    private String category;
    private Map<String, String> specs;
    private String condition;
}
