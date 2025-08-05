package com.example.stage2025.repository;

import com.example.stage2025.entity.ImportLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportLogRepository extends JpaRepository<ImportLog, Long> {
    List<ImportLog> findBySupplierId(Long supplierId);
}
