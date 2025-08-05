package com.example.stage2025.service;

import com.example.stage2025.dto.OrderDto;
import com.example.stage2025.enums.DeliveryStatus;
import com.example.stage2025.enums.OrderStatus;
import com.example.stage2025.enums.Status;

import java.util.List;

public interface OrderService {
    OrderDto createOrderFromCart(Long clientId, Long addressId, double shippingFee);
    List<OrderDto> getOrdersByClient(Long clientId);
    OrderDto getOrderById(Long orderId);
    void updateOrderStatus(Long orderId, OrderStatus status);
    void updateShippingStatus(Long orderId, DeliveryStatus shippingStatus);
}

