// src/main/java/com/example/stage2025/dto/SupplierProductDto.java
package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A slim DTO that represents a single product coming **from a supplier API**.
 * Used only during import/sync – it never goes to the front-end directly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierProductDto {
    private String externalProductId;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private List<String> imageUrls;
}
