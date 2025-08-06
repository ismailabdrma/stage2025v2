package com.example.stage2025.repository;

import com.example.stage2025.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // For Stripe/webhook/etc: Find payment by Stripe/PayPal transaction reference
    Optional<Payment> findByTransactionRef(String transactionRef);

    // Total of all completed payments
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED'")
    Double sumTotalPayments();
}
