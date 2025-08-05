// src/main/java/com/example/stage2025/dto/ProductDto.java
package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data sent to / from the front-end when listing or editing products.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double displayedPrice;

    // 🔥 real-time data pulled from supplier
    private Integer syncedStock;
    private Double dynamicPrice;
    private LocalDateTime lastFetched;

    private List<String> imageUrls;
    private String categoryName;
    private String supplierName;

    /** external product id at supplier (needed for sync, fetch, etc.) */
    private String externalProductId;     // <-- ADD THIS LINE
}

