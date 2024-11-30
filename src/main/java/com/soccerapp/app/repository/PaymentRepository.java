package com.soccerapp.app.repository;

import com.soccerapp.app.models.Payment;
import com.soccerapp.app.models.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Find payments by user ID
    List<Payment> findByUserId(Long userId);

    // Find payments by subscription plan ID
    List<Payment> findBySubscriptionPlanSubscriptionId(Long subscriptionId);

    // Find payments by payment status
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    // Find payments made after a specific date
    List<Payment> findByPaymentDateAfter(LocalDateTime date);

    // Find payments by transaction ID
    Optional<Payment> findByTransactionId(String transactionId);
}
