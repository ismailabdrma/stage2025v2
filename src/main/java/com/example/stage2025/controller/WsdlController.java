// src/main/java/com/example/stage2025/controller/WsdlController.java
package com.example.stage2025.controller;

import com.example.stage2025.dto.SoapOperationMeta;
import com.example.stage2025.utils.WsdlFullMetadataUtils;
import com.example.stage2025.utils.WsdlOperationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wsdl")
public class WsdlController {

    // Example: GET /api/wsdl/operations?url=http://localhost:8080/ws/supplier.wsdl
    @GetMapping("/operations")
    public ResponseEntity<List<String>> getWsdlOperations(@RequestParam String url) {
        try {
            List<String> ops = WsdlOperationUtils.getAllOperationNames(url);
            return ResponseEntity.ok(ops);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(List.of("ERROR: " + ex.getMessage()));
        }
    }
    @GetMapping("/operations-full")
    public ResponseEntity<List<SoapOperationMeta>> getWsdlOperationsFull(@RequestParam String url) {
        try {
            List<SoapOperationMeta> ops = WsdlFullMetadataUtils.getAllOperationMetadata(url);
            return ResponseEntity.ok(ops);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(List.of());
        }
    }
}
