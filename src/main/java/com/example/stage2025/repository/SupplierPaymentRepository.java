package com.example.stage2025.repository;

import com.example.stage2025.entity.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {
    // Find all payments for a specific supplier
    List<SupplierPayment> findBySupplierId(Long supplierId);

    // Optionally: find all by status
    List<SupplierPayment> findByStatus(com.example.stage2025.enums.PaymentStatus status);

}
