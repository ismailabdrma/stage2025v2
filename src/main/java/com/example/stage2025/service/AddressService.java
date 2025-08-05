package com.example.stage2025.service;

import com.example.stage2025.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto createAddress(Long clientId, AddressDto dto);
    List<AddressDto> getAddressesByClientId(Long clientId);
    AddressDto updateAddress(Long addressId, AddressDto dto);
    void deleteAddress(Long addressId);
}
