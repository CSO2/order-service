package com.CSO2.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "trade_in_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeInRequest {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String userId;
    private String deviceCategory;
    private String description;
    private BigDecimal estimatedValue;

    @Enumerated(EnumType.STRING)
    private TradeInStatus status;

    public enum TradeInStatus {
        PENDING, APPROVED, REJECTED
    }
}
