package com.example.stage2025.service;

import com.example.stage2025.dto.CartDto;
import com.example.stage2025.dto.CartItemDto;

public interface CartService {
    CartDto getCartByClientId(Long clientId);
    CartDto addItemToCart(Long clientId, CartItemDto itemDto);
    CartDto updateItemQuantity(Long cartItemId, int quantity);
    void removeItemFromCart(Long cartItemId);
    void clearCart(Long clientId);
}
