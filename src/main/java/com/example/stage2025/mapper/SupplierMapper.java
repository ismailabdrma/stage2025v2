// src/main/java/com/example/stage2025/mapper/SupplierMapper.java
package com.example.stage2025.mapper;

import com.example.stage2025.dto.*;
import com.example.stage2025.entity.*;
import java.util.stream.Collectors;

public class SupplierMapper {

    public static SupplierDto toDto(Supplier supplier) {
        SupplierDto dto = new SupplierDto();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setActive(supplier.isActive());
        dto.setCreated(supplier.getCreated());
        dto.setLastImport(supplier.getLastImport());
        dto.setPayoutFrequency(supplier.getPayoutFrequency());

        if (supplier instanceof ApiSupplier api) {
            dto.setType("API");
            dto.setApiUrl(api.getApiUrl());
            dto.setApiKey(api.getApiKey());
            dto.setAuthMethod(api.getAuthMethod());
        } else if (supplier instanceof ExcelSupplier excel) {
            dto.setType("EXCEL");
            dto.setFileName(excel.getFileName());
            dto.setFilePath(excel.getFilePath());
        } else if (supplier instanceof SoapSupplier soap) {
            dto.setType("SOAP");
            dto.setWsdlUrl(soap.getWsdlUrl());
            dto.setNamespace(soap.getNamespace());
            dto.setUsername(soap.getUsername());
            dto.setPassword(soap.getPassword());
            // Map dynamic SOAP ops
            if (soap.getOperations() != null) {
                dto.setOperationsMeta(
                        soap.getOperations().stream().map(SupplierMapper::toDto).collect(Collectors.toList())
                );
            }
        } else {
            dto.setType("BASE");
        }
        return dto;
    }

    public static SoapOperationMeta toDto(SoapSupplierOperationMeta op) {
        return new SoapOperationMeta(
                op.getOperationName(),
                op.getSoapAction(),
                op.getInputElement(),
                op.getOutputElement(),
                com.example.stage2025.dto.SoapFieldDto.fromJson(op.getInputFieldsJson()),
                com.example.stage2025.dto.SoapFieldDto.fromJson(op.getOutputFieldsJson())
        );
    }
}
