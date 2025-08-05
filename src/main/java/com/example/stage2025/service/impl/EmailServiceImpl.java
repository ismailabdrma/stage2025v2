package com.example.stage2025.service.impl;

import com.example.stage2025.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String otpCode) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("🔐 Votre code de vérification OTP");
        msg.setText(
                "Voici votre code OTP : " + otpCode +
                        "\nIl est valable pendant 10 minutes.\n\nMerci,\nL'équipe."
        );
        mailSender.send(msg);
    }
}
