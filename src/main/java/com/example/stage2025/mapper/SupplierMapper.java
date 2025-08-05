// src/main/java/com/example/stage2025/mapper/SupplierMapper.java
package com.example.stage2025.mapper;

import com.example.stage2025.dto.SupplierDto;
import com.example.stage2025.entity.*;

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
            dto.setOperation(soap.getOperation());
            dto.setNamespace(soap.getNamespace());
            dto.setUsername(soap.getUsername());
            dto.setPassword(soap.getPassword());
        } else {
            dto.setType("BASE");
        }
        return dto;
    }

    // You can add toEntity methods as needed
}
