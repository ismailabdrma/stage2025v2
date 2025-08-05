package com.example.stage2025.service.impl;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.entity.*;
import com.example.stage2025.enums.ImportStatus;
import com.example.stage2025.repository.CategoryRepository;
import com.example.stage2025.repository.ProductRepository;
import com.example.stage2025.repository.SupplierRepository;
import com.example.stage2025.service.ImportLogService;
import com.example.stage2025.service.ProductFetcherFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImportService {

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImportLogService importLogService;
    private final ProductFetcherFactory fetcherFactory;

    @Scheduled(fixedRate = 300_000) // every 5 min
    public void scheduledProductImport() {
        supplierRepository.findByActiveTrue().forEach(this::importProductsFromSupplier);
    }

    @Transactional
    public void importProductsFromSupplier(Supplier supplier) {
        log.info("Starting import for supplier: {}", supplier.getName());

        if (!(supplier instanceof ApiSupplier || supplier instanceof ExcelSupplier)) {
            log.warn("Unsupported supplier type: {}", supplier.getClass().getSimpleName());
            return;
        }

        try {
            List<SupplierProductDto> products = fetcherFactory.fetch((ApiSupplier) supplier);
            ImportStatus status = processImportedProducts(products, supplier);
            importLogService.saveLog(
                    supplier.getId(), status, products.size(),
                    status == ImportStatus.SUCCESS ? 0 : -1, null);

        } catch (Exception ex) {
            log.error("Import FAILED for supplier {}: {}", supplier.getName(), ex.getMessage());
            importLogService.saveLog(
                    supplier.getId(), ImportStatus.FAILED, 0, 0, ex.getMessage());
        }
    }

    @Transactional
    public void importProductsFromSupplier(Supplier supplier, List<SupplierProductDto> dtos) {
        processImportedProducts(dtos, supplier);
        importLogService.saveLog(supplier.getId(), ImportStatus.SUCCESS, dtos.size(), 0, null);
    }

    private ImportStatus processImportedProducts(List<SupplierProductDto> products, Supplier supplier) {
        int errorCount = 0;
        for (SupplierProductDto dto : products) {
            try {
                upsertProduct(dto, supplier);
            } catch (Exception e) {
                errorCount++;
                log.error("Error processing product {}: {}", dto.getExternalProductId(), e.getMessage());
            }
        }
        supplier.setLastImport(LocalDateTime.now());
        supplierRepository.save(supplier);

        return errorCount == 0 ? ImportStatus.SUCCESS : ImportStatus.PARTIAL;
    }

    private void upsertProduct(SupplierProductDto dto, Supplier supplier) {
        Product product = productRepository
                .findByExternalProductIdAndSupplier(dto.getExternalProductId(), supplier)
                .orElseGet(Product::new);

        boolean isNew = (product.getId() == null);

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSupplierPrice(dto.getPrice());
        product.setDynamicPrice(dto.getPrice());
        product.setSyncedStock(dto.getStock());
        product.setLastFetched(LocalDateTime.now());
        product.setImageUrls(dto.getImageUrls());
        product.setSupplier(supplier);

        // Ensure category
        if (product.getCategory() == null) {
            Category cat = categoryRepository.findByName("Uncategorized")
                    .orElseGet(() -> categoryRepository.save(
                            Category.builder()
                                    .name("Uncategorized")
                                    .description("Default category")
                                    .active(true)
                                    .build()
                    ));
            product.setCategory(cat);
        }

        // For new products: admin must approve (active=false)
        if (isNew) {
            product.setActive(false);
            product.setDisplayedPrice(dto.getPrice()); // Admin can edit before approval
        }
        productRepository.save(product);
    }
}
