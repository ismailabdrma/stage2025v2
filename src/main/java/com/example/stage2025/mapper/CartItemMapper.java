package com.example.stage2025.mapper;

import com.example.stage2025.dto.CartItemDto;
import com.example.stage2025.entity.CartItem;

public class CartItemMapper {
    public static CartItemDto toDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setProductName(item.getProduct().getName());
        dto.setProductId(item.getProduct().getId());
        return dto;
    }
}
