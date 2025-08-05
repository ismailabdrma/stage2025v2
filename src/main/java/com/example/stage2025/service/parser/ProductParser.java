package com.example.stage2025.service.parser;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.enums.DataFormat;

import java.util.List;

public interface ProductParser {

    /** Which DataFormat this implementation handles. */
    DataFormat supports();

    /** Parse the raw payload into DTOs. */
    List<SupplierProductDto> parse(byte[] payload) throws Exception;
}
