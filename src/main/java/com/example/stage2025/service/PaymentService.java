package com.example.stage2025.service;

import com.example.stage2025.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto dto);
    PaymentDto getPaymentById(Long id);
    List<PaymentDto> getAllPayments();
    PaymentDto updatePaymentStatus(Long id, String status);
}
