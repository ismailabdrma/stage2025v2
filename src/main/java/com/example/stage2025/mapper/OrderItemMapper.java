package com.example.stage2025.mapper;

import com.example.stage2025.dto.OrderItemDto;
import com.example.stage2025.entity.OrderItem;

public class OrderItemMapper {
    public static OrderItemDto toDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setProductName(item.getProduct().getName());
        return dto;
    }
}
