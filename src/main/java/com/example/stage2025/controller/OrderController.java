// src/main/java/com/example/stage2025/controller/OrderController.java
package com.example.stage2025.controller;

import com.example.stage2025.dto.OrderDto;
import com.example.stage2025.enums.DeliveryStatus;
import com.example.stage2025.enums.OrderStatus;
import com.example.stage2025.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Helper to get client ID from authentication
    private Long getClientId(Authentication auth) {
        // Change this logic to extract client ID correctly
        return Long.valueOf(auth.getName());
    }

    /** 1. Create an order from cart (client only) */
    @PostMapping("/create")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderDto> createOrder(Authentication auth, @RequestParam Long addressId, @RequestParam double shippingFee) {
        Long clientId = getClientId(auth);
        OrderDto dto = orderService.createOrderFromCart(clientId, addressId, shippingFee);
        return ResponseEntity.ok(dto);
    }

    /** 2. List all orders for current client */
    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<OrderDto>> getClientOrders(Authentication auth) {
        Long clientId = getClientId(auth);
        List<OrderDto> orders = orderService.getOrdersByClient(clientId);
        return ResponseEntity.ok(orders);
    }

    /** 3. Get order by ID (client can only access their own orders, admin can access any) */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
        // Add logic if you want to restrict access based on the user
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    /** 4. Update order status (admin only) */
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok("Order status updated");
    }

    /** 5. Update shipping status (admin only) */
    @PatchMapping("/{orderId}/shipping-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateShippingStatus(@PathVariable Long orderId, @RequestParam DeliveryStatus shippingStatus) {
        orderService.updateShippingStatus(orderId, shippingStatus);
        return ResponseEntity.ok("Shipping status updated");
    }
}
