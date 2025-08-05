package com.example.stage2025.service.parser;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.enums.DataFormat;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvProductParser implements ProductParser {

    @Override public DataFormat supports() { return DataFormat.CSV; }

    @Override
    public List<SupplierProductDto> parse(byte[] payload) throws Exception {
        List<SupplierProductDto> list = new ArrayList<>();
        try (Reader r = new InputStreamReader(new ByteArrayInputStream(payload))) {
            for (CSVRecord rec : CSVFormat.DEFAULT
                    .withHeader("externalId","name","description","price","stock","images")
                    .withFirstRecordAsHeader()
                    .parse(r)) {
                list.add(new SupplierProductDto(
                        rec.get("externalId"),
                        rec.get("name"),
                        rec.get("description"),
                        Double.valueOf(rec.get("price")),
                        Integer.valueOf(rec.get("stock")),
                        List.of(rec.get("images").split("\\|"))));
            }
        }
        return list;
    }
}
