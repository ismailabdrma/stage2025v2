package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PaymentDto {
    private Long id;
    private Double amount;
    private String status;
    private String transactionRef;
    private String paymentMethod;
    private LocalDateTime paymentDate;
}
