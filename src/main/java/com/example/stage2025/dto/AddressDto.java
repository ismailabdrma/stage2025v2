package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressDto {
    private Long id;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private boolean DefaultAddress;
}
