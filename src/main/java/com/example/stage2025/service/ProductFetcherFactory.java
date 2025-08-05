// src/main/java/com/example/stage2025/service/ProductFetcherFactory.java
package com.example.stage2025.service;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.entity.ApiSupplier;
import com.example.stage2025.entity.ExcelSupplier;
import com.example.stage2025.entity.SoapSupplier;
import com.example.stage2025.entity.Supplier;
import com.example.stage2025.enums.DataFormat;
import com.example.stage2025.service.parser.ProductParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFetcherFactory {

    private final WebClient web;
    private final List<ProductParser> parsers;
    private final SoapProductFetcher soapProductFetcher;

    /** Universal fetch for any supplier type */
    public List<SupplierProductDto> fetch(Supplier supplier) throws Exception {
        if (supplier instanceof ApiSupplier api) {
            return fetchApiSupplier(api);
        } else if (supplier instanceof ExcelSupplier excel) {
            return fetchExcelSupplier(excel);
        } else if (supplier instanceof SoapSupplier soap) {
            return soapProductFetcher.fetchProducts(soap);
        }
        throw new IllegalArgumentException("Unsupported supplier type: " + supplier.getClass().getSimpleName());
    }

    /** Fetch and parse for API supplier */
    private List<SupplierProductDto> fetchApiSupplier(ApiSupplier s) throws Exception {
        byte[] payload = web.get()
                .uri(s.getApiUrl() + s.getProductsEndpoint())
                .header(HttpHeaders.AUTHORIZATION, authHeader(s))
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
        return parserFor(s.getDataFormat()).parse(payload);
    }

    /** Fetch and parse for Excel supplier */
    private List<SupplierProductDto> fetchExcelSupplier(ExcelSupplier s) throws Exception {
        if (s.getFilePath() == null) throw new IllegalStateException("No Excel filePath for supplier: " + s.getName());
        byte[] payload = Files.readAllBytes(Paths.get(s.getFilePath()));
        return parserFor(DataFormat.EXCEL).parse(payload);
    }

    // Fetch single product for API suppliers (real-time)
    public SupplierProductDto fetchOne(ApiSupplier s, String externalId) throws Exception {
        List<SupplierProductDto> list = parsePayload(
                s,
                s.getSingleEndpoint().replace("{externalId}", externalId)
        );
        if (list.isEmpty())
            throw new IllegalStateException("Product not found in supplier feed");
        return list.get(0);
    }

    // Helper for single product API fetch
    private List<SupplierProductDto> parsePayload(ApiSupplier s, String path) throws Exception {
        byte[] payload = web.get()
                .uri(s.getApiUrl() + path)
                .header(HttpHeaders.AUTHORIZATION, authHeader(s))
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
        return parserFor(s.getDataFormat()).parse(payload);
    }

    private ProductParser parserFor(DataFormat fmt) {
        return parsers.stream()
                .filter(p -> p.supports() == fmt)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No parser for " + fmt));
    }

    private String authHeader(ApiSupplier s) {
        if (s.getApiKey() == null) return "";
        return "BEARER".equalsIgnoreCase(s.getAuthMethod())
                ? "Bearer " + s.getApiKey()
                : s.getApiKey();
    }
}
