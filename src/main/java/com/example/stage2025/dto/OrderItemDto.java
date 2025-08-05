package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderItemDto {
    private Long id;
    private Integer quantity;
    private Double unitPrice;
    private String productName;

}
