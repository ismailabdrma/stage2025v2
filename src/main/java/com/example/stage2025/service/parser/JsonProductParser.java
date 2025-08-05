package com.example.stage2025.service.parser;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.enums.DataFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class JsonProductParser implements ProductParser {

    private final ObjectMapper om = new ObjectMapper();

    @Override public DataFormat supports() { return DataFormat.JSON; }

    @Override
    public List<SupplierProductDto> parse(byte[] payload) throws Exception {
        return Arrays.asList(om.readValue(payload, SupplierProductDto[].class));
    }
}
