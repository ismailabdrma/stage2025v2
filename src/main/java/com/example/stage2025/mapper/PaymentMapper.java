// src/main/java/com/example/stage2025/mapper/PaymentMapper.java
package com.example.stage2025.mapper;

import com.example.stage2025.dto.PaymentDto;
import com.example.stage2025.entity.Payment;

public class PaymentMapper {

    public static PaymentDto toDto(Payment payment) {
        if (payment == null) return null;
        return PaymentDto.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .transactionRef(payment.getTransactionRef())
                .paymentMethod(payment.getPaymentMethod())
                .paymentDate(payment.getPaymentDate())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                .build();
    }

    public static Payment toEntity(PaymentDto dto) {
        Payment p = new Payment();
        p.setId(dto.getId());
        p.setAmount(dto.getAmount());
        p.setStatus(dto.getStatus());
        p.setTransactionRef(dto.getTransactionRef());
        p.setPaymentMethod(dto.getPaymentMethod());
        p.setPaymentDate(dto.getPaymentDate());
        // Do NOT set Order here! Link in service.
        return p;
    }
}
