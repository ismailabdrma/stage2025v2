package com.example.stage2025.repository;

import com.example.stage2025.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByClientId(Long clientId);

  Optional<Order> findByOrderNumber(String orderNumber);
}
