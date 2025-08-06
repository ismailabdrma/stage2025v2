// src/main/java/com/example/stage2025/dto/PaymentDto.java
package com.example.stage2025.dto;

import com.example.stage2025.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Long id;
    private Double amount;
    private PaymentStatus status;
    private String transactionRef;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long orderId; // <--- ADD THIS
}

