package com.example.stage2025.service;

import com.example.stage2025.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto dto);
    ProductDto updateProduct(Long id, ProductDto dto);
    void deleteProduct(Long id);

    // Service Interface
    List<ProductDto> getProductsByCategoryName(String categoryName);
    List<ProductDto> getProductsBySupplierName(String supplierName);
    List<ProductDto> getProductsByCategoryAndSupplier(String categoryName, String supplierName);

}
