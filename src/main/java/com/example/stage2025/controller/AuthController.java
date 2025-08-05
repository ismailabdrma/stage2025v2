package com.example.stage2025.controller;

import com.example.stage2025.entity.Admin;
import com.example.stage2025.entity.Client;
import com.example.stage2025.entity.User;
import com.example.stage2025.enums.Role;
import com.example.stage2025.repository.UserRepository;
import com.example.stage2025.security.JwtUtils;
import com.example.stage2025.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authManager;
    private final OtpService otpService;

    // --- DTOs ---
    public record SignupRequest(String username, String email, String password, Role role) {}
    public record LoginRequest(String identifier, String password) {}
    public record JwtResponse(String token, String username, String email, String role) {}
    public record OtpVerifyRequest(String email, String otp) {}
    public record ResendOtpRequest(String email, String type) {}
    public record EmailRequest(String email) {}
    public record ResetPasswordRequest(String email, String otp, String newPassword) {}
    public record ApiResponse(String status, String message) {}

    /** --- 1. Registration --- */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        if (userRepo.existsByUsernameOrEmail(req.username(), req.email())) {
            return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "Username or email already exists"));
        }

        User user = (req.role() == Role.ADMIN) ? new Admin() : new Client();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(encoder.encode(req.password()));
        user.setRole(req.role());
        user.setActive(false);

        userRepo.save(user);
        otpService.sendOtp(user.getEmail(), user.getId(), "REGISTER");

        return ResponseEntity.ok(new ApiResponse("OTP_REQUIRED", "User registered. Please verify your email with the OTP sent."));
    }

    /** --- 2. Verify Email OTP (activate account) --- */
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody OtpVerifyRequest req) {
        Optional<User> userOpt = userRepo.findByEmail(req.email());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "Invalid email"));

        User user = userOpt.get();
        if (otpService.verifyOtp(user.getEmail(), "REGISTER", req.otp())) {
            user.setActive(true);
            userRepo.save(user);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", "Email verified. You may now log in."));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "Invalid or expired OTP."));
        }
    }

    /** --- 3. Login + 2FA (OTP sent after password is correct) --- */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(req.identifier(), req.password());
            authManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("ERROR", "Invalid username or password"));
        }

        User user = userRepo.findByUsernameOrEmail(req.identifier(), req.identifier())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.isActive()) {
            return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "Account not activated."));
        }

        otpService.sendOtp(user.getEmail(), user.getId(), "LOGIN");
        return ResponseEntity.ok(new ApiResponse("OTP_REQUIRED", "OTP sent to your email. Please verify to complete login."));
    }

    /** --- 4. Complete Login with OTP (return JWT) --- */
    @PostMapping("/login/verify-otp")
    public ResponseEntity<?> loginVerifyOtp(@RequestBody OtpVerifyRequest req) {
        Optional<User> userOpt = userRepo.findByEmail(req.email());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "Invalid email"));

        User user = userOpt.get();
        if (otpService.verifyOtp(user.getEmail(), "LOGIN", req.otp())) {
            String token = jwtUtils.generateToken(user);
            return ResponseEntity.ok(new JwtResponse(token, user.getUsername(), user.getEmail(), user.getRole().name()));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "Invalid or expired OTP."));
        }
    }

    /** --- 5. Resend OTP (for login or registration verification) --- */
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody ResendOtpRequest req) {
        Optional<User> userOpt = userRepo.findByEmail(req.email());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "User not found"));

        User user = userOpt.get();
        otpService.sendOtp(user.getEmail(), user.getId(), req.type());
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "OTP resent to email."));
    }

    /** --- 6. Forgot Password: request reset (send OTP) --- */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailRequest req) {
        Optional<User> userOpt = userRepo.findByEmail(req.email());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "User not found"));

        User user = userOpt.get();
        otpService.sendOtp(user.getEmail(), user.getId(), "FORGOT");
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "OTP sent for password reset."));
    }

    /** --- 7. Reset Password with OTP --- */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest req) {
        Optional<User> userOpt = userRepo.findByEmail(req.email());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "User not found"));

        User user = userOpt.get();
        if (otpService.verifyOtp(user.getEmail(), "FORGOT", req.otp())) {
            user.setPassword(encoder.encode(req.newPassword()));
            userRepo.save(user);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", "Password reset successful."));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse("ERROR", "Invalid or expired OTP."));
        }
    }
}
