package com.example.stage2025.dto;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class SoapOperationMeta {
    private String operationName;
    private String soapAction;
    private String inputElement;
    private String outputElement;
    private List<SoapFieldDto> inputFields;
    private List<SoapFieldDto> outputFields;
}
