package com.example.stage2025.mapper;

import com.example.stage2025.dto.PaymentDto;
import com.example.stage2025.entity.Payment;

public class PaymentMapper {
    public static PaymentDto toDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus().name());
        dto.setTransactionRef(payment.getTransactionRef());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }
}
