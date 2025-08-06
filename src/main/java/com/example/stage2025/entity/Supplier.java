// src/main/java/com/example/stage2025/entity/Supplier.java
package com.example.stage2025.entity;

import com.example.stage2025.enums.PayoutFrequency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "supplier_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean active;
    private LocalDateTime created;
    private LocalDateTime lastImport;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<ImportLog> importLogs;

    @OneToMany(mappedBy = "supplier")
    private List<Product> products;

    @Enumerated(EnumType.STRING)
    private PayoutFrequency payoutFrequency = PayoutFrequency.WEEKLY;

    // For batch payout tracking
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<SupplierPayment> payments;
}
