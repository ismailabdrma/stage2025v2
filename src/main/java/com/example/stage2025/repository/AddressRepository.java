package com.example.stage2025.repository;

import com.example.stage2025.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByClientId(Long clientId);}
