package com.example.stage2025.service.impl;

import com.example.stage2025.dto.OrderDto;
import com.example.stage2025.entity.*;
import com.example.stage2025.enums.DeliveryStatus;
import com.example.stage2025.enums.OrderStatus;
import com.example.stage2025.exception.ResourceNotFoundException;
import com.example.stage2025.mapper.OrderMapper;
import com.example.stage2025.repository.*;
import com.example.stage2025.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository; // ADD THIS

    @Override
    @Transactional
    public OrderDto createOrderFromCart(Long clientId, Long addressId, double shippingFee) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        Cart cart = cartRepository.findByClientId(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        double subtotal = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        double total = subtotal + shippingFee;

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .shippingStatus(DeliveryStatus.NOT_SHIPPED)
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .total(total)
                .client(client)
                .deliveryAddress(address)
                .comments("")
                .build();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(item -> OrderItem.builder()
                        .order(order)
                        .product(item.getProduct())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getQuantity() * item.getUnitPrice())
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        // === Update product stocks: reduce both syncedStock and reservedStock ===
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            int orderedQty = cartItem.getQuantity();
            int syncedStock = product.getSyncedStock() != null ? product.getSyncedStock() : 0;
            int reservedStock = product.getReservedStock() != null ? product.getReservedStock() : 0;

            product.setSyncedStock(syncedStock - orderedQty);
            product.setReservedStock(reservedStock - orderedQty);

            productRepository.save(product); // make sure to persist!
        }

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        Order saved = orderRepository.save(order);
        return OrderMapper.toDto(saved);
    }

    @Override
    public List<OrderDto> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientId(clientId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return OrderMapper.toDto(order);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public void updateShippingStatus(Long orderId, DeliveryStatus shippingStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setShippingStatus(shippingStatus);
        orderRepository.save(order);
    }
}
