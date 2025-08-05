package com.example.stage2025.service.impl;

import com.example.stage2025.entity.OtpToken;
import com.example.stage2025.repository.OtpTokenRepository;
import com.example.stage2025.service.EmailService;
import com.example.stage2025.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpTokenRepository otpRepo;
    private final EmailService emailService;

    private static final int EXPIRY_MINUTES = 10;

    @Override
    public void sendOtp(String email, Long userId, String type) {
        String otpCode = String.valueOf(100_000 + new Random().nextInt(900_000));
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(EXPIRY_MINUTES);

        OtpToken token = OtpToken.builder()
                .email(email)
                .userId(userId)
                .otp(otpCode)
                .expiresAt(expiresAt)
                .type(type)
                .build();
        otpRepo.save(token);

        emailService.sendOtpEmail(email, otpCode); // <<== Only one method, clear name!
    }

    @Override
    public boolean verifyOtp(String email, String type, String otp) {
        return otpRepo.findTopByEmailAndTypeOrderByExpiresAtDesc(email, type)
                .filter(t -> t.getOtp().equals(otp) && t.getExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
