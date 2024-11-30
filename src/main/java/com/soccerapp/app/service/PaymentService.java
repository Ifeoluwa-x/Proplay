package com.soccerapp.app.service;

import com.soccerapp.app.models.Payment;
import com.soccerapp.app.models.Payment.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment saveSuccessPayment(Long userId, Long subscriptionId, String transId);

    Optional<Payment> getPaymentById(Long paymentId);

    List<Payment> getPaymentsByUserId(Long userId);

    List<Payment> getPaymentsBySubscriptionPlanId(Long subscriptionId);

    List<Payment> getPaymentsByStatus(PaymentStatus paymentStatus);

    List<Payment> getPaymentsAfterDate(LocalDateTime date);

    Optional<Payment> getPaymentByTransactionId(String transactionId);

    void updatePaymentStatus(Long paymentId, PaymentStatus paymentStatus);

    void deletePayment(Long paymentId);
}
