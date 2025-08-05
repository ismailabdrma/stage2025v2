package com.example.stage2025.controller;

import com.example.stage2025.dto.SupplierPaymentDto;
import com.example.stage2025.service.SupplierPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-payments")
@RequiredArgsConstructor
public class SupplierPaymentController {

    private final SupplierPaymentService supplierPaymentService;

    // 1. Get all payments (admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupplierPaymentDto>> getAll() {
        return ResponseEntity.ok(supplierPaymentService.getAllPayments());
    }

    // 2. Get payments by supplier
    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupplierPaymentDto>> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierPaymentService.getPaymentsBySupplierId(supplierId));
    }

    // 3. Get payment by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierPaymentDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierPaymentService.getPaymentById(id));
    }

    // 4. Create new payment (manual payout)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierPaymentDto> create(@RequestBody SupplierPaymentDto dto) {
        return ResponseEntity.ok(supplierPaymentService.createPayment(dto));
    }
}
