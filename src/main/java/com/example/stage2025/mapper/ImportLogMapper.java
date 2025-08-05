package com.example.stage2025.mapper;

import com.example.stage2025.dto.ImportLogDto;
import com.example.stage2025.entity.ImportLog;

public class ImportLogMapper {
    public static ImportLogDto toDto(ImportLog log) {
        ImportLogDto dto = new ImportLogDto();
        dto.setId(log.getId());
        dto.setImportDate(log.getImportDate());
        dto.setStatus(log.getStatus());
        dto.setImportedCount(log.getImportedCount());
        dto.setErrorCount(log.getErrorCount());
        dto.setErrorMessage(log.getErrorMessage());
        dto.setSupplierId(log.getSupplier().getId());
        return dto;
    }
}
