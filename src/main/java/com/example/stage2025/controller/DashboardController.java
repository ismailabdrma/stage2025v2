// src/main/java/com/example/stage2025/controller/DashboardController.java
package com.example.stage2025.controller;

import com.example.stage2025.dto.DashboardStatsDto;
import com.example.stage2025.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final PaymentRepository paymentRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDto> getStats() {
        DashboardStatsDto stats = new DashboardStatsDto(
                userRepository.count(),
                productRepository.count(),
                orderRepository.count(),
                supplierRepository.count(),
                paymentRepository.count(),
                paymentRepository.sumTotalPayments() // Optional: custom query for total sales
        );
        return ResponseEntity.ok(stats);
    }
}
