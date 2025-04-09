package com.example.ecomm_orderservice.repository;

import com.example.ecomm_orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
