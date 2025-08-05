// src/main/java/com/example/stage2025/service/impl/ImportLogServiceImpl.java
package com.example.stage2025.service.impl;

import com.example.stage2025.dto.ImportLogDto;
import com.example.stage2025.entity.ImportLog;
import com.example.stage2025.entity.Supplier;
import com.example.stage2025.enums.ImportStatus;
import com.example.stage2025.exception.ResourceNotFoundException;
import com.example.stage2025.mapper.ImportLogMapper;
import com.example.stage2025.repository.ImportLogRepository;
import com.example.stage2025.repository.SupplierRepository;
import com.example.stage2025.service.ImportLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportLogServiceImpl implements ImportLogService {

    private final ImportLogRepository importLogRepository;
    private final SupplierRepository   supplierRepository;

    /* ---------- read methods ---------- */

    @Override
    public List<ImportLogDto> getLogsBySupplierId(Long supplierId) {
        if (!supplierRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException("Supplier not found: id=" + supplierId);
        }
        return importLogRepository.findBySupplierId(supplierId)
                .stream()
                .map(ImportLogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ImportLogDto getLogById(Long id) {
        ImportLog log = importLogRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Import log not found: id=" + id));
        return ImportLogMapper.toDto(log);
    }

    /* ---------- write method ---------- */

    @Override
    public ImportLogDto saveLog(Long supplierId,
                                ImportStatus status,
                                int importedCount,
                                int errorCount,
                                String errorMessage) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found: id=" + supplierId));

        ImportLog log = ImportLog.builder()
                .importDate(LocalDateTime.now())
                .status(status.name())      // write enum value as String
                .importedCount(importedCount)
                .errorCount(errorCount)
                .errorMessage(errorMessage)
                .supplier(supplier)
                .build();

        return ImportLogMapper.toDto(importLogRepository.save(log));
    }
}
