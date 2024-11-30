package com.soccerapp.app.service.impl;

import com.soccerapp.app.models.*;
import com.soccerapp.app.models.Payment.PaymentStatus;
import com.soccerapp.app.repository.PaymentRepository;
import com.soccerapp.app.repository.SubscriptionPlanRepository;
import com.soccerapp.app.repository.UserRepository;
import com.soccerapp.app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository, SubscriptionPlanRepository planRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Override
    public Payment saveSuccessPayment(Long userId, Long subscriptionId, String transId) {
        // Fetch the User and Player objects from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        SubscriptionPlan plan = planRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription plan not found with id " + subscriptionId));



        // Create and set up the Payment object
        Payment payment = new Payment();
        payment.setUser(user);  // Set the actual User object
        payment.setSubscriptionPlan(plan);  // Set the actual plan object
        payment.setAmount(plan.getPrice());  // Use the predefined custom message
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId(transId);

        // Save the team request to the database
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public List<Payment> getPaymentsBySubscriptionPlanId(Long subscriptionId) {
        return paymentRepository.findBySubscriptionPlanSubscriptionId(subscriptionId);
    }

    @Override
    public List<Payment> getPaymentsByStatus(PaymentStatus paymentStatus) {
        return paymentRepository.findByPaymentStatus(paymentStatus);
    }

    @Override
    public List<Payment> getPaymentsAfterDate(LocalDateTime date) {
        return paymentRepository.findByPaymentDateAfter(date);
    }

    @Override
    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus paymentStatus) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
//            payment.setPaymentStatus(paymentStatus);
            paymentRepository.save(payment);
        } else {
            throw new IllegalArgumentException("Payment not found with ID: " + paymentId);
        }
    }

    @Override
    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
