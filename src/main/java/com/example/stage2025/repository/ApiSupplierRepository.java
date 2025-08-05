package com.example.stage2025.repository;

import com.example.stage2025.entity.ApiSupplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiSupplierRepository extends JpaRepository<ApiSupplier, Long> {

    Optional<ApiSupplier> findByName(String name);}
