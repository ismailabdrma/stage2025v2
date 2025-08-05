package com.example.stage2025.entity;

import com.example.stage2025.enums.DataFormat;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("API")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ApiSupplier extends Supplier {

    private String apiUrl;
    private String authMethod;          // BEARER, BASIC, NONE
    private String apiKey;

    private DataFormat dataFormat = DataFormat.JSON;

    private String productsEndpoint = "/products";
    private String singleEndpoint   ;

}
