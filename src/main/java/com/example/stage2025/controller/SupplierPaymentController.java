package com.example.stage2025.controller;

import com.example.stage2025.dto.SupplierPaymentDto;
import com.example.stage2025.service.SupplierPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles supplier payout management (admin only).
 */
@RestController
@RequestMapping("/api/supplier-payments")
@RequiredArgsConstructor
public class SupplierPaymentController {

    private final SupplierPaymentService supplierPaymentService;

    /**
     * List all supplier payments (admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupplierPaymentDto>> getAll() {
        return ResponseEntity.ok(supplierPaymentService.getAllPayments());
    }

    /**
     * List payments by supplier ID (admin only)
     */
    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupplierPaymentDto>> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierPaymentService.getPaymentsBySupplierId(supplierId));
    }

    /**
     * Get a specific supplier payment by ID (admin only)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierPaymentDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierPaymentService.getPaymentById(id));
    }

    /**
     * Create a new manual supplier payout (admin only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierPaymentDto> create(@RequestBody SupplierPaymentDto dto) {
        return ResponseEntity.ok(supplierPaymentService.createPayment(dto));
    }
}
