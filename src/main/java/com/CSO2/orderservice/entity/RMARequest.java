package com.CSO2.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "rma_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RMARequest {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String orderId;
    private String userId;
    private String reason;

    @Enumerated(EnumType.STRING)
    private RMAStatus status;

    public enum RMAStatus {
        REQUESTED, APPROVED, REFUNDED
    }
}
