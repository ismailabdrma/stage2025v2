package com.example.stage2025.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;        // Ex: "CREATE_ORDER", "PAYMENT_SUCCESS"
    private String entity;        // Ex: "Order", "Payment"
    private Long entityId;        // ID of the target entity

    private String userEmail;     // Optional - for audit purposes
    private Long userId;          // Admin or Client ID
    private String userType;      // "ADMIN" or "CLIENT"

    private String details;       // Optional description (JSON, etc.)
    private LocalDateTime actionDate;
}
