package com.example.stage2025.service.impl;

import com.example.stage2025.dto.PaymentDto;
import com.example.stage2025.entity.Payment;
import com.example.stage2025.enums.PaymentStatus;
import com.example.stage2025.enums.Status;
import com.example.stage2025.exception.ResourceNotFoundException;
import com.example.stage2025.mapper.PaymentMapper;
import com.example.stage2025.repository.PaymentRepository;
import com.example.stage2025.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto createPayment(PaymentDto dto) {
        Payment payment = Payment.builder()
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .transactionRef(dto.getTransactionRef())
                .paymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDateTime.now())
                .status(PaymentStatus.PENDING) // statut par défaut
                .build();

        return PaymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé : ID = " + id));
        return PaymentMapper.toDto(payment);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDto updatePaymentStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé : ID = " + id));
        payment.setStatus(PaymentStatus.valueOf(status.toUpperCase()));
        return PaymentMapper.toDto(paymentRepository.save(payment));
    }
}
