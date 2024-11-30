package com.soccerapp.app.controller;

import com.soccerapp.app.dto.SubscriptionPlanDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Payment;
import com.soccerapp.app.models.SubscriptionPlan;
import com.soccerapp.app.models.User;
import com.soccerapp.app.service.PaymentService;
import com.soccerapp.app.service.StripeService;
import com.soccerapp.app.service.SubscriptionPlanService;
import com.soccerapp.app.service.UserService;
import com.soccerapp.app.utils.Response;
import com.stripe.model.forwarding.Request;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class SubscriptionPlanController {

    private StripeService stripeService;
    private UserService userService;
    private PaymentService paymentService;

    public SubscriptionPlanController(StripeService stripeService, UserService userService, PaymentService paymentService) {
        this.stripeService = stripeService;
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;

    @Value("${stripe.key.public}")
    private String API_PUBLIC_KEY;

    @GetMapping("/subscription/plans")
    public String getAllSubscriptionPlans(Model model, HttpSession session) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        List<SubscriptionPlanDto> subscriptionPlans = subscriptionPlanService.getAllPlans();

        model.addAttribute("user", loggedInUser);
        model.addAttribute("subscriptionPlans", subscriptionPlans);
        return "subscription-plans"; // Thymeleaf template name
    }

    @PostMapping("/subscription-plans/{planId}/checkout")
    public String chargePage(@PathVariable Long planId, Model model, HttpSession session) {

        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        // Retrieve the selected plan using the ID
        SubscriptionPlan selectedPlan = subscriptionPlanService.getPlanById(planId);

        if (selectedPlan == null) {
            model.addAttribute("error", "Selected plan not found!");
            return "subscription-plans"; // Return to the subscription plans page
        }

        // If no errors, proceed to the checkout page
        model.addAttribute("amount", selectedPlan.getPrice());  // Assuming the price is a part of SubscriptionPlan
        model.addAttribute("email", loggedInUser.getEmail() );  // Replace with actual user email
        model.addAttribute("planId", planId);
        model.addAttribute("productName", selectedPlan.getPlanName());  // Use the plan name

        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
        return "charge";
    }

    @PostMapping("/create-charge")
    public @ResponseBody ResponseEntity<Response> createCharge(HttpSession session,
                                                               String email,
                                                               String token,
                                                               Long amount,
                                                               Long planId) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        SubscriptionPlan selectedPlan = subscriptionPlanService.getPlanById(planId);

        Long userId = loggedInUser.getId();
        Long subscriptionId = selectedPlan.getSubscriptionId();

        if (token == null) {
            return ResponseEntity.badRequest().body(new Response(false, "Stripe payment token is missing. Please try again."));
        }

        if (amount == null || amount <= 0L) {
            return ResponseEntity.badRequest().body(new Response(false, "Invalid amount specified."));
        }

        Long centedAmount = amount * 100L;
        String chargeId = stripeService.createCharge(email, token, centedAmount);

        if (chargeId == null) {
            // Customize the error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Payment failed. Please try another card or contact support."));
        }

        // Redirect to the payment success endpoint
        String redirectUrl = "/payment/success/" + userId + "/" + subscriptionId + "/" + chargeId;
        return ResponseEntity.ok(new Response(true, "Success! You have successfully subscribed!", redirectUrl));
    }

}