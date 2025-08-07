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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    /**
     * Get all products, or filter by category and/or supplier (as query params).
     * Ex: /api/products?category=Smartphones&supplier=Samsung&search=iphone&sortBy=price,desc&page=0&size=10&includeInactive=true
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "supplier", required = false) String supplier,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sortBy", required = false, defaultValue = "name,asc") String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "includeInactive", required = false, defaultValue = "false") boolean includeInactive
    ) {
        Sort sort = Sort.by(sortBy.split(",")[0]);
        if (sortBy.split(",").length > 1 && sortBy.split(",")[1].equalsIgnoreCase("desc")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productService.getAllProducts(category, supplier, search, includeInactive, pageable);
        List<ProductDto> products = productPage.getContent().stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());

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

    // --- Admin: Activate product ---
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(true);
        productRepository.save(product);
        return ResponseEntity.ok("Product activated");
    }

    // --- Admin: Deactivate product ---
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
        return ResponseEntity.ok("Product deactivated");
    }

    // --- Public: Get featured products ---
    @GetMapping("/featured")
    public ResponseEntity<List<ProductDto>> getFeaturedProducts() {
        List<ProductDto> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(products);
    }

    // --- Public: Get suggested products ---
    @GetMapping("/{id}/suggested")
    public ResponseEntity<List<ProductDto>> getSuggestedProducts(@PathVariable Long id) {
        List<ProductDto> products = productService.getSuggestedProducts(id);
        return ResponseEntity.ok(products);
    }
}
