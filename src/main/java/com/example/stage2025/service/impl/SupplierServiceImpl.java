package com.example.stage2025.service.impl;

import com.example.stage2025.dto.SupplierDto;
import com.example.stage2025.entity.ApiSupplier;
import com.example.stage2025.entity.ExcelSupplier;
import com.example.stage2025.entity.SoapSupplier;
import com.example.stage2025.entity.SoapSupplierOperationMeta;
import com.example.stage2025.entity.Supplier;
import com.example.stage2025.exception.ResourceNotFoundException;
import com.example.stage2025.mapper.SupplierMapper;
import com.example.stage2025.repository.SoapSupplierOperationMetaRepository;
import com.example.stage2025.repository.SupplierRepository;
import com.example.stage2025.service.SupplierService;
import com.example.stage2025.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SoapSupplierOperationMetaRepository operationMetaRepository;

    @Override
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(SupplierMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDto getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        return SupplierMapper.toDto(supplier);
    }

    @Override
    public SupplierDto createSupplier(SupplierDto dto) {
        Supplier supplier;

        if ("API".equalsIgnoreCase(dto.getType())) {
            ApiSupplier apiSupplier = new ApiSupplier();
            apiSupplier.setName(dto.getName());
            apiSupplier.setActive(dto.isActive());
            apiSupplier.setCreated(LocalDateTime.now());
            apiSupplier.setApiUrl(dto.getApiUrl());
            apiSupplier.setApiKey(dto.getApiKey());
            apiSupplier.setAuthMethod(dto.getAuthMethod());
            supplier = apiSupplier;

        } else if ("EXCEL".equalsIgnoreCase(dto.getType())) {
            ExcelSupplier excelSupplier = new ExcelSupplier();
            excelSupplier.setName(dto.getName());
            excelSupplier.setActive(dto.isActive());
            excelSupplier.setCreated(LocalDateTime.now());
            excelSupplier.setFileName(dto.getFileName());
            excelSupplier.setFilePath(dto.getFilePath());
            supplier = excelSupplier;

        } else if ("SOAP".equalsIgnoreCase(dto.getType())) {
            SoapSupplier soapSupplier = new SoapSupplier();
            soapSupplier.setName(dto.getName());
            soapSupplier.setActive(dto.isActive());
            soapSupplier.setCreated(LocalDateTime.now());
            soapSupplier.setWsdlUrl(dto.getWsdlUrl());
            soapSupplier.setNamespace(dto.getNamespace());
            soapSupplier.setUsername(dto.getUsername());
            soapSupplier.setPassword(dto.getPassword());
            soapSupplier.setOperations(new ArrayList<>()); // Will populate later
            supplier = soapSupplier;

        } else {
            throw new IllegalArgumentException("Unknown supplier type: " + dto.getType());
        }

        // Save supplier (must be saved before adding operations)
        Supplier saved = supplierRepository.save(supplier);

        // ---- Only for SOAP suppliers: Add operation metadata if provided ----
        if (saved instanceof SoapSupplier && dto.getOperationsMeta() != null) {
            SoapSupplier soap = (SoapSupplier) saved;
            List<SoapSupplierOperationMeta> ops = dto.getOperationsMeta().stream()
                    .map(metaDto -> {
                        SoapSupplierOperationMeta op = new SoapSupplierOperationMeta();
                        op.setOperationName(metaDto.getOperationName());
                        op.setInputElement(metaDto.getInputElement());
                        op.setOutputElement(metaDto.getOutputElement());
                        op.setSoapAction(metaDto.getSoapAction());
                        op.setInputFieldsJson(JsonUtils.toJson(metaDto.getInputFields()));
                        op.setOutputFieldsJson(JsonUtils.toJson(metaDto.getOutputFields()));

                        op.setSupplier(soap);
                        return op;
                    })
                    .collect(Collectors.toList());
            operationMetaRepository.saveAll(ops);
            soap.setOperations(ops);
            supplierRepository.save(soap); // Update supplier with ops
        }

        return SupplierMapper.toDto(supplierRepository.save(saved));
    }

    @Override
    public SupplierDto updateSupplier(Long id, SupplierDto dto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        supplier.setName(dto.getName());
        supplier.setActive(dto.isActive());

        if (supplier instanceof ApiSupplier && "API".equalsIgnoreCase(dto.getType())) {
            ApiSupplier api = (ApiSupplier) supplier;
            api.setApiUrl(dto.getApiUrl());
            api.setApiKey(dto.getApiKey());
            api.setAuthMethod(dto.getAuthMethod());

        } else if (supplier instanceof ExcelSupplier && "EXCEL".equalsIgnoreCase(dto.getType())) {
            ExcelSupplier excel = (ExcelSupplier) supplier;
            excel.setFileName(dto.getFileName());
            excel.setFilePath(dto.getFilePath());

        } else if (supplier instanceof SoapSupplier && "SOAP".equalsIgnoreCase(dto.getType())) {
            SoapSupplier soap = (SoapSupplier) supplier;
            soap.setWsdlUrl(dto.getWsdlUrl());
            soap.setNamespace(dto.getNamespace());
            soap.setUsername(dto.getUsername());
            soap.setPassword(dto.getPassword());

            // (Optional) Update operations meta as well
            if (dto.getOperationsMeta() != null) {
                // Remove old
                operationMetaRepository.deleteAll(soap.getOperations());
                // Add new
                List<SoapSupplierOperationMeta> ops = dto.getOperationsMeta().stream()
                        .map(metaDto -> {
                            SoapSupplierOperationMeta op = new SoapSupplierOperationMeta();
                            op.setOperationName(metaDto.getOperationName());
                            op.setInputElement(metaDto.getInputElement());
                            op.setOutputElement(metaDto.getOutputElement());
                            op.setSoapAction(metaDto.getSoapAction());
                            op.setInputFieldsJson(JsonUtils.toJson(metaDto.getInputFields()));
                            op.setOutputFieldsJson(JsonUtils.toJson(metaDto.getOutputFields()));

                            op.setSupplier(soap);
                            return op;
                        })
                        .collect(Collectors.toList());
                operationMetaRepository.saveAll(ops);
                soap.setOperations(ops);
            }
        }

        return SupplierMapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found");
        }
        supplierRepository.deleteById(id);
    }

    @Override
    public void activateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        supplier.setActive(true);
        supplierRepository.save(supplier);
    }

    @Override
    public void deactivateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }
}
