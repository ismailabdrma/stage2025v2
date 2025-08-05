package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

@Data
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private Double unitPrice;
    private String productName;
    private Long productId;}

