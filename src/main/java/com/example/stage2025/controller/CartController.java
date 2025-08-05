// src/main/java/com/example/stage2025/controller/CartController.java
package com.example.stage2025.controller;

import com.example.stage2025.dto.CartDto;
import com.example.stage2025.dto.CartItemDto;
import com.example.stage2025.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Helper to get client ID from authentication (adapt as needed)
    private Long getClientId(Authentication auth) {
        // You can extract clientId from JWT claims or UserDetails.
        // Here, assume username == clientId (as String).
        return Long.valueOf(auth.getName());
    }

    /** 1. Get current user's cart */
    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication auth) {
        Long clientId = getClientId(auth);
        return ResponseEntity.ok(cartService.getCartByClientId(clientId));
    }

    /** 2. Add item to cart */
    @PostMapping("/add")
    public ResponseEntity<CartDto> addItem(Authentication auth, @RequestBody CartItemDto itemDto) {
        Long clientId = getClientId(auth);
        return ResponseEntity.ok(cartService.addItemToCart(clientId, itemDto));
    }

    /** 3. Update item quantity */
    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartDto> updateQuantity(Authentication auth, @PathVariable Long cartItemId, @RequestParam int quantity) {
        // You may want to check if the item belongs to the user's cart.
        return ResponseEntity.ok(cartService.updateItemQuantity(cartItemId, quantity));
    }

    /** 4. Remove item from cart */
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<?> removeItem(Authentication auth, @PathVariable Long cartItemId) {
        cartService.removeItemFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }

    /** 5. Clear all items from cart */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication auth) {
        Long clientId = getClientId(auth);
        cartService.clearCart(clientId);
        return ResponseEntity.noContent().build();
    }
}
