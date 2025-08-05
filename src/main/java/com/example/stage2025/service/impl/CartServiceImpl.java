package com.example.stage2025.service.impl;

import com.example.stage2025.dto.CartDto;
import com.example.stage2025.dto.CartItemDto;
import com.example.stage2025.entity.*;
import com.example.stage2025.exception.ResourceNotFoundException;
import com.example.stage2025.mapper.CartMapper;
import com.example.stage2025.repository.*;
import com.example.stage2025.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CartDto getCartByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec ID " + clientId));
        Cart cart = client.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setClient(client);
            cartRepository.save(cart);
        }
        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto addItemToCart(Long clientId, CartItemDto itemDto) {
        Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec ID " + itemDto.getProductId()));
        if (product.getSyncedStock() == null) throw new IllegalStateException("Stock not initialized");
        int reserved = product.getReservedStock() != null ? product.getReservedStock() : 0;
        int available = product.getSyncedStock() - reserved;

        if (itemDto.getQuantity() > available) {
            throw new IllegalArgumentException("Not enough stock. Available: " + available);
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec ID " + clientId));
        Cart cart = Optional.ofNullable(client.getCart()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setClient(client);
            return cartRepository.save(newCart);
        });

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(itemDto.getQuantity());
        item.setUnitPrice(product.getDisplayedPrice());
        cartItemRepository.save(item);

        // Reserve stock
        product.setReservedStock(reserved + itemDto.getQuantity());
        productRepository.save(product);

        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto updateItemQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item de panier introuvable"));

        Product product = item.getProduct();
        int reserved = product.getReservedStock() != null ? product.getReservedStock() : 0;
        int currentQty = item.getQuantity();
        int diff = quantity - currentQty;

        if (diff > 0) {
            int available = product.getSyncedStock() - reserved;
            if (diff > available) {
                throw new IllegalArgumentException("Not enough stock to increase quantity.");
            }
        }
        item.setQuantity(quantity);
        cartItemRepository.save(item);

        product.setReservedStock(reserved + diff);
        productRepository.save(product);

        return CartMapper.toDto(item.getCart());
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item introuvable"));
        Product product = item.getProduct();
        int reserved = product.getReservedStock() != null ? product.getReservedStock() : 0;
        product.setReservedStock(reserved - item.getQuantity());
        productRepository.save(product);

        cartItemRepository.delete(item);
    }

    @Override
    @Transactional
    public void clearCart(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));
        Cart cart = client.getCart();
        if (cart != null) {
            for (CartItem item : cart.getItems()) {
                Product product = item.getProduct();
                int reserved = product.getReservedStock() != null ? product.getReservedStock() : 0;
                product.setReservedStock(reserved - item.getQuantity());
                productRepository.save(product);
            }
            cartItemRepository.deleteAll(cart.getItems());
        }
    }
}
