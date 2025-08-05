// src/main/java/com/example/stage2025/mapper/ProductMapper.java
package com.example.stage2025.mapper;

import com.example.stage2025.dto.ProductDto;
import com.example.stage2025.entity.Product;

public class ProductMapper {

    public static ProductDto toDto(Product p) {
        if (p == null) return null;

        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setDisplayedPrice(p.getDisplayedPrice());

        // real-time cache fields
        dto.setSyncedStock(p.getSyncedStock());
        dto.setDynamicPrice(p.getDynamicPrice());
        dto.setLastFetched(p.getLastFetched());

        dto.setImageUrls(p.getImageUrls());
        dto.setCategoryName(p.getCategory() != null ? p.getCategory().getName() : null);
        dto.setSupplierName(p.getSupplier() != null ? p.getSupplier().getName() : null);

        dto.setExternalProductId(p.getExternalProductId()); // <-- ADD THIS LINE

        return dto;
    }

}
