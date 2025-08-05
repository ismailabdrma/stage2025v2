// src/main/java/com/example/stage2025/entity/SoapSupplier.java
package com.example.stage2025.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("SOAP")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SoapSupplier extends Supplier {
    private String wsdlUrl;     // URL of the WSDL file
    private String operation;   // SOAP operation to call, e.g., "GetProducts"
    private String namespace;   // SOAP namespace, needed for request
    private String username;    // Optional (if auth needed)
    private String password;    // Optional (if auth needed)
}
