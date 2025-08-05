package com.example.stage2025.mapper;

import com.example.stage2025.dto.OrderDto;
import com.example.stage2025.entity.Order;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().name());
        dto.setShippingStatus(order.getShippingStatus().name());
        dto.setTotal(order.getTotal());
        dto.setClientEmail(order.getClient().getEmail());
        dto.setItems(order.getItems().stream()
                .map(OrderItemMapper::toDto)
                .collect(Collectors.toList()));
        return dto;
    }
}