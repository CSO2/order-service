package com.CSO2.orderservice.repository;

import com.CSO2.orderservice.entity.RMARequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RMARepository extends JpaRepository<RMARequest, String> {
    List<RMARequest> findByOrderId(String orderId);
}
