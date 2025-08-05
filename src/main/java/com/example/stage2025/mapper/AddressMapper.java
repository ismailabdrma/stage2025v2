
package com.example.stage2025.mapper;

import com.example.stage2025.dto.AddressDto;
import com.example.stage2025.entity.Address;

public class AddressMapper {

    public static AddressDto toDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setZipCode(address.getZipCode());
        dto.setCountry(address.getCountry());
        dto.setDefaultAddress(address.isDefaultAddress());
        return dto;
    }

    public static Address toEntity(AddressDto dto) {
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setZipCode(dto.getZipCode());
        address.setCountry(dto.getCountry());
        address.setDefaultAddress(dto.isDefaultAddress());
        return address;
    }
}
