package com.example.stage2025.service.parser;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.enums.DataFormat;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class XmlProductParser implements ProductParser {

    private final XmlMapper xml = new XmlMapper();

    @Override public DataFormat supports() { return DataFormat.XML; }

    @Override
    public List<SupplierProductDto> parse(byte[] payload) throws Exception {
        return Arrays.asList(xml.readValue(payload, SupplierProductDto[].class));
    }
}
