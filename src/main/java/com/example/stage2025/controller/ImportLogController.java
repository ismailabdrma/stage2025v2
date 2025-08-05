// src/main/java/com/example/stage2025/controller/ImportLogController.java
package com.example.stage2025.controller;

import com.example.stage2025.dto.ImportLogDto;
import com.example.stage2025.service.ImportLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-logs")
@RequiredArgsConstructor
public class ImportLogController {

    private final ImportLogService importLogService;

    // 1. List logs by supplier
    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImportLogDto>> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(importLogService.getLogsBySupplierId(supplierId));
    }

    // 2. Get import log by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImportLogDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(importLogService.getLogById(id));
    }
}
