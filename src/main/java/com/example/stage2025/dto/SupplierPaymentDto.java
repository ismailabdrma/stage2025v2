package com.example.stage2025.dto;

import com.example.stage2025.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierPaymentDto {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private String description;
}
