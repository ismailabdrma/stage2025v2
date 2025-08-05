package com.example.stage2025.repository;

import com.example.stage2025.entity.Category;
import com.example.stage2025.entity.Product;
import com.example.stage2025.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByCategoryId(Long categoryId);

  List<Product> findBySupplierId(Long supplierId);

  List<Product> findByCategory(Category category);

  Optional<Product> findByExternalProductIdAndSupplier(String externalProductId,
                                                       Supplier supplier);

  List<Product> findByActiveFalse();
}
