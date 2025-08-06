package com.example.stage2025.controller;

import com.example.stage2025.dto.ProductDto;
import com.example.stage2025.entity.Product;
import com.example.stage2025.mapper.ProductMapper;
import com.example.stage2025.repository.ProductRepository;
import com.example.stage2025.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    /**
     * Get all products, or filter by category and/or supplier (as query params).
     * Ex: /api/products?category=Smartphones&supplier=Samsung
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "supplier", required = false) String supplier
    ) {
        List<ProductDto> products;

        if (category != null && supplier != null) {
            products = productService.getProductsByCategoryAndSupplier(category, supplier);
        } else if (category != null) {
            products = productService.getProductsByCategoryName(category);
        } else if (supplier != null) {
            products = productService.getProductsBySupplierName(supplier);
        } else {
            products = productService.getAllProducts();
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // --- Admin: List all inactive (pending validation) products ---
    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> getInactiveProducts() {
        List<Product> inactive = productRepository.findByActiveFalse();
        List<ProductDto> dtos = inactive.stream().map(ProductMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    // --- Admin: Activate/validate product ---
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(true);
        productRepository.save(product);
        return ResponseEntity.ok("Product activated");
    }
}
