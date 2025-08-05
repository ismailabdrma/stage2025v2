package com.example.stage2025.repository;

import com.example.stage2025.entity.Cart;
import com.example.stage2025.entity.Category;
import com.example.stage2025.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByClientId(Long clientId);

}
