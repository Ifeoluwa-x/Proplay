package com.soccerapp.app.service;


import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;

@Service
public class StripeService {

    @Value("${stripe.key.secret}")
    private String API_SECET_KEY;

    public StripeService() {

    }

    public String createCustomer(String email, String token) {

        String id = null;

        try {
            Stripe.apiKey = API_SECET_KEY;
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("description", "Customer for " + email);
            customerParams.put("email", email);
            // obtained with stripe.js
            customerParams.put("source", token);

            Customer customer = Customer.create(customerParams);
            id = customer.getId();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public String createSubscription(String customerId, String plan, String coupon) {

        String subscriptionId = null;

        try {
            Stripe.apiKey = API_SECET_KEY;

            Map<String, Object> item = new HashMap<>();
            item.put("plan", plan);

            Map<String, Object> items = new HashMap<>();
            items.put("0", item);

            Map<String, Object> params = new HashMap<>();
            params.put("customer", customerId);
            params.put("items", items);

            if (!coupon.isEmpty()) {
                params.put("coupon", coupon);
            }

            Subscription subscription = Subscription.create(params);

            subscriptionId = subscription.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subscriptionId;
    }

    public boolean cancelSubscription(String subscriptionId) {

        boolean subscriptionStatus;

        try {
            Subscription subscription = Subscription.retrieve(subscriptionId);
            subscription.cancel();
            subscriptionStatus = true;
        } catch (Exception e) {
            e.printStackTrace();
            subscriptionStatus = false;
        }
        return subscriptionStatus;
    }

    public Coupon retriveCoupon(String code) {
        try {
            Stripe.apiKey = API_SECET_KEY;
            return Coupon.retrieve(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createCharge(String email, String token, Long amount) {
        String chargeId = null;

        try {
            // Set the Stripe API key
            Stripe.apiKey = API_SECET_KEY;

            // Create charge parameters
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("description", "Charge for " + email);
            chargeParams.put("currency", "usd");
            chargeParams.put("amount", amount);
            chargeParams.put("source", token);

            // Create the charge
            Charge charge = Charge.create(chargeParams);

            // Get the charge ID
            chargeId = charge.getId();
        } catch (CardException e) {
            String errorMessage = "Payment failed. Details - Status: " + e.getStatusCode() +
                    ", Code: " + e.getCode() +
                    ", Param: " + e.getParam() +
                    ", Message: " + e.getMessage();
            // Instead of throwing an exception, return the error message
            return errorMessage; // Return the error message to be handled by the controller
        // Or use a custom exception like PaymentException
        } catch (RateLimitException e) {
            // Too many requests made to the API too quickly
            System.err.println("Rate limit exception: " + e.getMessage());
        } catch (InvalidRequestException e) {
            // Invalid parameters were supplied to Stripe's API
            System.err.println("Invalid request: " + e.getMessage());
        } catch (AuthenticationException e) {
            // Authentication with Stripe's API failed
            System.err.println("Authentication exception: " + e.getMessage());
        } catch (StripeException e) {
            // Generic Stripe exception
            System.err.println("Stripe exception: " + e.getMessage());
        } catch (Exception e) {
            // Other exceptions
            System.err.println("Exception: " + e.getMessage());
        }

        return chargeId;
    }


}
