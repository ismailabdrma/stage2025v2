package com.example.stage2025.service.parser;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.enums.DataFormat;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelProductParser implements ProductParser {

    @Override public DataFormat supports() { return DataFormat.EXCEL; }

    @Override
    public List<SupplierProductDto> parse(byte[] payload) throws Exception {
        List<SupplierProductDto> list = new ArrayList<>();
        try (Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(payload))) {
            Sheet sh = wb.getSheetAt(0);
            for (Row row : sh) {
                if (row.getRowNum() == 0) continue; // header
                list.add(new SupplierProductDto(
                        row.getCell(0).getStringCellValue(),         // externalId
                        row.getCell(1).getStringCellValue(),         // name
                        row.getCell(2).getStringCellValue(),         // description
                        row.getCell(3).getNumericCellValue(),        // price
                        (int) row.getCell(4).getNumericCellValue(),  // stock
                        List.of(row.getCell(5).getStringCellValue().split("\\|")) // images
                ));
            }
        }
        return list;
    }
}
