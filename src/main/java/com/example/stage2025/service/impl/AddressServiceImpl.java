package com.example.stage2025.service.impl;

import com.example.stage2025.dto.AddressDto;
import com.example.stage2025.entity.Address;
import com.example.stage2025.entity.Client;
import com.example.stage2025.exception.ResourceNotFoundException;
import com.example.stage2025.mapper.AddressMapper;
import com.example.stage2025.repository.AddressRepository;
import com.example.stage2025.repository.ClientRepository;
import com.example.stage2025.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;

    @Override
    public AddressDto createAddress(Long clientId, AddressDto dto) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Address address = AddressMapper.toEntity(dto);
        address.setClient(client);
        Address saved = addressRepository.save(address);

        return AddressMapper.toDto(saved);
    }

    @Override
    public List<AddressDto> getAddressesByClientId(Long clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        return addressRepository.findByClientId(clientId).stream()
                .map(AddressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto updateAddress(Long addressId, AddressDto dto) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setZipCode(dto.getZipCode());
        address.setCountry(dto.getCountry());
        address.setDefaultAddress(dto.isDefaultAddress());

        Address updated = addressRepository.save(address);
        return AddressMapper.toDto(updated);
    }

    @Override
    public void deleteAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new ResourceNotFoundException("Address not found");
        }
        addressRepository.deleteById(addressId);
    }
}
