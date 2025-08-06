package com.example.stage2025.controller;

import com.example.stage2025.dto.SoapOperationMeta;
import com.example.stage2025.dto.SoapFieldDto;
import com.example.stage2025.entity.SoapSupplier;
import com.example.stage2025.repository.SupplierRepository;
import com.example.stage2025.utils.SoapDynamicInvoker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/soap")
@RequiredArgsConstructor
public class DynamicSoapController {

    private final SupplierRepository supplierRepository;

    @PostMapping("/call/{supplierId}/{operationName}")
    public ResponseEntity<?> callSoapOperation(
            @PathVariable Long supplierId,
            @PathVariable String operationName,
            @RequestBody(required = false) Map<String, Object> body) {

        try {
            SoapSupplier supplier = (SoapSupplier) supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));

            // Find the operation meta
            SoapOperationMeta opMeta = supplier.getOperations().stream()
                    .filter(op -> op.getOperationName().equalsIgnoreCase(operationName))
                    .findFirst()
                    .map(op -> new SoapOperationMeta(
                            op.getOperationName(),
                            op.getSoapAction(),
                            op.getInputElement(),
                            op.getOutputElement(),
                            SoapFieldDto.fromJson(op.getInputFieldsJson()),
                            SoapFieldDto.fromJson(op.getOutputFieldsJson())
                    ))
                    .orElseThrow(() -> new RuntimeException("Operation not found"));

            // === Add complexType mapping for product! ===
            Map<String, List<SoapFieldDto>> complexTypes = new HashMap<>();
            complexTypes.put("product", List.of(
                    new SoapFieldDto("id", "xs:long"),
                    new SoapFieldDto("name", "xs:string"),
                    new SoapFieldDto("description", "xs:string"),
                    new SoapFieldDto("supplierPrice", "xs:double"),
                    new SoapFieldDto("displayedPrice", "xs:double"),
                    new SoapFieldDto("availableQuantity", "xs:int"),
                    new SoapFieldDto("pictureUrl", "xs:string"),
                    new SoapFieldDto("realTimeStock", "xs:int"),
                    new SoapFieldDto("lastStockUpdate", "xs:string")
            ));

            Map<String, Object> inputValues = (body != null && body.containsKey("input"))
                    ? (Map<String, Object>) body.get("input")
                    : Map.of();

            Map<String, Object> result = SoapDynamicInvoker.call(
                    supplier.getWsdlUrl(),
                    supplier.getNamespace(),
                    opMeta,
                    inputValues,
                    complexTypes
            );
            return ResponseEntity.ok(result);

        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/operations/{supplierId}")
    public ResponseEntity<?> getOperations(@PathVariable Long supplierId) {
        SoapSupplier supplier = (SoapSupplier) supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return ResponseEntity.ok(
                supplier.getOperations().stream().map(op -> new SoapOperationMeta(
                        op.getOperationName(),
                        op.getSoapAction(),
                        op.getInputElement(),
                        op.getOutputElement(),
                        SoapFieldDto.fromJson(op.getInputFieldsJson()),
                        SoapFieldDto.fromJson(op.getOutputFieldsJson())
                )).toList()
        );
    }
}
