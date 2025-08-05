package com.example.stage2025.service;

import com.example.stage2025.dto.ImportLogDto;
import com.example.stage2025.enums.ImportStatus;

import java.util.List;

public interface ImportLogService {
    List<ImportLogDto> getLogsBySupplierId(Long supplierId);
    ImportLogDto getLogById(Long id);

    ImportLogDto saveLog(Long supplierId,
                         ImportStatus status,
                         int importedCount,
                         int errorCount,
                         String errorMessage);
}

