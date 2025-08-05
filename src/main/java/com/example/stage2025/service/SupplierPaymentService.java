package com.example.stage2025.service;

import com.example.stage2025.dto.SupplierPaymentDto;

import java.util.List;

public interface SupplierPaymentService {
    List<SupplierPaymentDto> getAllPayments();
    List<SupplierPaymentDto> getPaymentsBySupplierId(Long supplierId);
    SupplierPaymentDto getPaymentById(Long id);
    SupplierPaymentDto createPayment(SupplierPaymentDto dto);
    // Optionally: Add update/delete methods if needed
}
