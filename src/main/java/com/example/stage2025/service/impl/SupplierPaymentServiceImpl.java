package com.example.stage2025.service.impl;

import com.example.stage2025.dto.SupplierPaymentDto;
import com.example.stage2025.entity.Supplier;
import com.example.stage2025.entity.SupplierPayment;
import com.example.stage2025.enums.PaymentStatus;
import com.example.stage2025.mapper.SupplierPaymentMapper;
import com.example.stage2025.repository.SupplierPaymentRepository;
import com.example.stage2025.repository.SupplierRepository;
import com.example.stage2025.service.SupplierPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierPaymentServiceImpl implements SupplierPaymentService {

    private final SupplierPaymentRepository paymentRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public List<SupplierPaymentDto> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(SupplierPaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierPaymentDto> getPaymentsBySupplierId(Long supplierId) {
        return paymentRepository.findBySupplierId(supplierId)
                .stream()
                .map(SupplierPaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierPaymentDto getPaymentById(Long id) {
        SupplierPayment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return SupplierPaymentMapper.toDto(payment);
    }

    @Override
    public SupplierPaymentDto createPayment(SupplierPaymentDto dto) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        SupplierPayment payment = SupplierPayment.builder()
                .supplier(supplier)
                .amount(dto.getAmount())
                .status(dto.getStatus() != null ? dto.getStatus() : PaymentStatus.PENDING)
                .paymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDateTime.now())
                .description(dto.getDescription())
                .build();

        return SupplierPaymentMapper.toDto(paymentRepository.save(payment));
    }
}
