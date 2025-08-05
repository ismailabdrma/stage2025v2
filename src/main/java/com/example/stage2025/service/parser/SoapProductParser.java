// src/main/java/com/example/stage2025/service/parser/SoapProductParser.java
package com.example.stage2025.service.parser;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.enums.DataFormat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SoapProductParser implements ProductParser {
    @Override
    public DataFormat supports() { return DataFormat.SOAP; }

    @Override
    public List<SupplierProductDto> parse(byte[] payload) throws Exception {
        // TODO: In the future, implement with a SOAP client and parse the response
        throw new UnsupportedOperationException("SOAP parsing not implemented yet!");
    }
}
