package com.example.stage2025.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoapSupplierOperationMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String operationName;
    private String inputElement;
    private String outputElement;
    private String soapAction;

    @Column(columnDefinition = "TEXT")
    private String inputFieldsJson;

    @Column(columnDefinition = "TEXT")
    private String outputFieldsJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private SoapSupplier supplier;
}
