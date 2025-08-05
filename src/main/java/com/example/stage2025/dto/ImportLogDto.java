package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ImportLogDto {
    private Long id;
    private LocalDateTime importDate;
    private String status;
    private int importedCount;
    private int errorCount;
    private String errorMessage;
    private Long supplierId;}
