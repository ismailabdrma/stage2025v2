// src/main/java/com/example/stage2025/controller/ProductController.java
package com.example.stage2025.controller;

import com.example.stage2025.dto.ProductDto;
import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.entity.ApiSupplier;
import com.example.stage2025.enums.Role;
import com.example.stage2025.repository.ApiSupplierRepository;
import com.example.stage2025.service.ProductFetcherFactory;
import com.example.stage2025.service.ProductService;
import jakarta.annotation.security.RolesAllowed;
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
    private final ApiSupplierRepository apiSupplierRepository;
    private final ProductFetcherFactory fetcherFactory;

    // 1. List all products (public)
    @GetMapping
    public ResponseEntity<List<ProductDto>> listAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 2. Get product details by ID (public)
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 3. List by category (public)
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<ProductDto>> getByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(productService.getProductsByCategoryName(categoryName));
    }

    // 4. Create product (admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    // 5. Update product (admin only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    // 6. Delete product (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // 7. Real-time stock/price fetch from supplier for this product (admin only)
    @GetMapping("/{id}/sync")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierProductDto> syncFromSupplier(@PathVariable Long id) throws Exception {
        ProductDto product = productService.getProductById(id);
        // Assume ProductDto includes supplierName and externalProductId fields
        ApiSupplier supplier = apiSupplierRepository.findByName(product.getSupplierName())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        SupplierProductDto dto = fetcherFactory.fetchOne(supplier, product.getExternalProductId());
        return ResponseEntity.ok(dto);
    }
}
