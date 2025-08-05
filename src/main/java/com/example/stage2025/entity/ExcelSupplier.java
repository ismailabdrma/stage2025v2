package com.example.stage2025.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("EXCEL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelSupplier extends Supplier {
    private String fileName;
    private String filePath;
}
