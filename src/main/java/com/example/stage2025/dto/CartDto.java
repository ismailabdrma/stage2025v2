package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartDto {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<CartItemDto> items;

}