package com.example.stage2025.entity;

import com.example.stage2025.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PAID, PENDING, FAILED

    private LocalDateTime paymentDate;

    private String description;
}
