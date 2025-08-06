package com.example.stage2025.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@DiscriminatorValue("SOAP")
@NoArgsConstructor @AllArgsConstructor
public class SoapSupplier extends Supplier {
    private String wsdlUrl;
    private String namespace;
    private String username;
    private String password;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SoapSupplierOperationMeta> operations = new ArrayList<>();
}
