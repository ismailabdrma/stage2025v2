package com.example.stage2025.service;

public interface OtpService {
    void sendOtp(String email, Long userId, String type);          // Generate & send OTP
    boolean verifyOtp(String email, String type, String otp);       // Check code
}
