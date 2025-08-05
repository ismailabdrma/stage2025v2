// src/main/java/com/example/stage2025/service/impl/ApiProductFetcher.java
package com.example.stage2025.service.impl;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.entity.ApiSupplier;
import com.example.stage2025.entity.Supplier;
import com.example.stage2025.service.ProductFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiProductFetcher implements ProductFetcher {

    private final WebClient webClient;

    @Override
    public List<SupplierProductDto> fetchProducts(Supplier supplier) throws Exception {
        if (!(supplier instanceof ApiSupplier api)) {
            throw new IllegalArgumentException("Supplier is not API based");
        }

        return webClient.get()
                .uri(api.getApiUrl())
                .header(HttpHeaders.AUTHORIZATION, authHeader(api))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(SupplierProductDto.class)
                .collectList()
                .block();            // <-- blocking call
    }

    /* ------------------------------------------------------------------ */

    private String authHeader(ApiSupplier api) {
        return switch (api.getAuthMethod().toUpperCase()) {
            case "BEARER" -> "Bearer " + api.getApiKey();
            case "BASIC"  -> "Basic " + java.util.Base64.getEncoder()
                    .encodeToString(api.getApiKey().getBytes());
            default       -> api.getApiKey();
        };
    }
}
