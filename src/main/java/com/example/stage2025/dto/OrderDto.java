package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderDto {
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String status;
    private String shippingStatus;
    private Double total;
    private String clientEmail;
    private List<OrderItemDto> items;
}
