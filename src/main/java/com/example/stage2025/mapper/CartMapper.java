package com.example.stage2025.mapper;

import com.example.stage2025.dto.CartDto;
import com.example.stage2025.entity.Cart;

import java.util.stream.Collectors;

public class CartMapper {
    public static CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setCreatedDate(cart.getCreatedDate());
        dto.setUpdatedDate(cart.getUpdatedDate());
        dto.setItems(cart.getItems().stream()
                .map(CartItemMapper::toDto)
                .collect(Collectors.toList()));
        return dto;
    }}
