// src/main/java/com/example/stage2025/repository/SoapSupplierOperationMetaRepository.java
package com.example.stage2025.repository;

import com.example.stage2025.entity.SoapSupplierOperationMeta;
import com.example.stage2025.entity.SoapSupplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SoapSupplierOperationMetaRepository extends JpaRepository<SoapSupplierOperationMeta, Long> {
    List<SoapSupplierOperationMeta> findBySupplier(SoapSupplier supplier);
}
