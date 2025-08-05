package com.example.stage2025.repository;

import com.example.stage2025.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findTopByEmailAndTypeOrderByExpiresAtDesc(String email, String type);
    void deleteByEmail(String email); // optional, to clear used tokens
}
