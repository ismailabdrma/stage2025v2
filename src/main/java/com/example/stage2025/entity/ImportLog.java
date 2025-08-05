package com.example.stage2025.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime importDate;

    private String status; // SUCCESS, FAILED

    private Integer importedCount;
    private Integer errorCount;
    private String errorMessage;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}
