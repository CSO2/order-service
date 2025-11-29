package com.CSO2.orderservice.repository;

import com.CSO2.orderservice.entity.TradeInRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeInRepository extends JpaRepository<TradeInRequest, String> {
}
