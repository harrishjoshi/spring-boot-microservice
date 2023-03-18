package com.harrishjoshi.orderservice.repository;

import com.harrishjoshi.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
