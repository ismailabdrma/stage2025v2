package com.example.stage2025.mapper;

import com.example.stage2025.dto.SupplierPaymentDto;
import com.example.stage2025.entity.SupplierPayment;

public class SupplierPaymentMapper {

    public static SupplierPaymentDto toDto(SupplierPayment payment) {
        if (payment == null) return null;
        return SupplierPaymentDto.builder()
                .id(payment.getId())
                .supplierId(payment.getSupplier() != null ? payment.getSupplier().getId() : null)
                .supplierName(payment.getSupplier() != null ? payment.getSupplier().getName() : null)
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .description(payment.getDescription())
                .build();
    }

    public static SupplierPayment toEntity(SupplierPaymentDto dto) {
        if (dto == null) return null;
        SupplierPayment payment = new SupplierPayment();
        payment.setId(dto.getId());
        // Set supplier manually in service, as you typically load it from DB
        payment.setAmount(dto.getAmount());
        payment.setStatus(dto.getStatus());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setDescription(dto.getDescription());
        return payment;
    }
}
