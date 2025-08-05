package com.example.stage2025.service;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.entity.ApiSupplier;
import com.example.stage2025.entity.Supplier;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductFetcher {

    List<SupplierProductDto> fetchProducts(Supplier supplier) throws Exception;
}
