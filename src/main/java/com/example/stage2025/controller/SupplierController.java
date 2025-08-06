package com.example.stage2025.controller;

import com.example.stage2025.dto.SupplierDto;
import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.dto.SoapOperationMeta;
import com.example.stage2025.entity.Supplier;
import com.example.stage2025.repository.SupplierRepository;
import com.example.stage2025.service.ProductFetcherFactory;
import com.example.stage2025.service.impl.ProductImportService;
import com.example.stage2025.service.SupplierService;
import com.example.stage2025.utils.WsdlFullMetadataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final ProductFetcherFactory fetcherFactory;
    private final SupplierRepository supplierRepository;
    private final SupplierService supplierService;
    private final ProductImportService productImportService;

    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAll() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierDto> create(@RequestBody SupplierDto dto) {
        return ResponseEntity.ok(supplierService.createSupplier(dto));
    }

    // --- Create SOAP supplier & auto-extract WSDL metadata ---
    @PostMapping("/auto-soap")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierDto> createSoapAuto(@RequestBody SupplierDto dto) throws Exception {
        if (!"SOAP".equalsIgnoreCase(dto.getType())) {
            throw new IllegalArgumentException("Type must be SOAP");
        }
        List<SoapOperationMeta> ops = WsdlFullMetadataUtils.getAllOperationMetadata(dto.getWsdlUrl());
        dto.setOperationsMeta(ops);
        return ResponseEntity.ok(supplierService.createSupplier(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierDto> update(@PathVariable Long id, @RequestBody SupplierDto dto) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activate(@PathVariable Long id) {
        supplierService.activateSupplier(id);
        return ResponseEntity.ok().body("Supplier activated");
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        supplierService.deactivateSupplier(id);
        return ResponseEntity.ok().body("Supplier deactivated");
    }

    // --- Sync/preview products (does not save) ---
    @GetMapping("/{id}/sync-products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupplierProductDto>> syncProducts(@PathVariable Long id) throws Exception {
        var supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        List<SupplierProductDto> products = fetcherFactory.fetch(supplier);
        return ResponseEntity.ok(products);
    }

    // --- NEW: Import & save products from supplier (SOAP/API/Excel) ---
    @PostMapping("/{id}/import-products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importProducts(@PathVariable Long id) {
        var supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        productImportService.importProductsFromSupplier(supplier);
        return ResponseEntity.ok("Import started for supplier " + supplier.getName());
    }
}
