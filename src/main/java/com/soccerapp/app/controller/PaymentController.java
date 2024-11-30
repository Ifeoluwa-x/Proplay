package com.soccerapp.app.controller;

import com.soccerapp.app.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stripe.model.Coupon;
import com.soccerapp.app.service.StripeService;
import com.soccerapp.app.utils.Response;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PaymentController {

    @Value("${stripe.key.public}")
    private String API_PUBLIC_KEY;

    private StripeService stripeService;
    private PaymentService paymentService;

    public PaymentController(StripeService stripeService,PaymentService paymentService) {
        this.stripeService = stripeService;
        this.paymentService = paymentService;
    }

//    @GetMapping("/")
//    public String homepage() {
//        return "homepage";
//    }

//    @GetMapping("/subscription")
//    public String subscriptionPage(Model model) {
//        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
//        return "subscription";
//    }

    @GetMapping("/payment/success/{userId}/{subscriptionId}/{chargeId}")
    public String paymentSuccess(@PathVariable Long userId,
                                 @PathVariable Long subscriptionId,
                                 @PathVariable String chargeId,
                                 Model model) {
        paymentService.saveSuccessPayment(userId, subscriptionId, chargeId);
        return "redirect:/create/user/subscription" + userId + "/" + subscriptionId;
    }


//    @PostMapping("/create-subscription")
//    public @ResponseBody Response createSubscription(String email, String token, String plan, String coupon) {
//
//        if (token == null || plan.isEmpty()) {
//            return new Response(false, "Stripe payment token is missing. Please try again later.");
//        }
//
//        String customerId = stripeService.createCustomer(email, token);
//
//        if (customerId == null) {
//            return new Response(false, "An error accurred while trying to create customer");
//        }
//
//        String subscriptionId = stripeService.createSubscription(customerId, plan, coupon);
//
//        if (subscriptionId == null) {
//            return new Response(false, "An error accurred while trying to create subscription");
//        }
//
//        return new Response(true, "Success! your subscription id is " + subscriptionId);
//    }
//
//    @PostMapping("/cancel-subscription")
//    public @ResponseBody Response cancelSubscription(String subscriptionId) {
//
//        boolean subscriptionStatus = stripeService.cancelSubscription(subscriptionId);
//
//        if (!subscriptionStatus) {
//            return new Response(false, "Faild to cancel subscription. Please try again later");
//        }
//
//        return new Response(true, "Subscription cancelled successfully");
//    }
//
//    @PostMapping("/coupon-validator")
//    public @ResponseBody Response couponValidator(String code) {
//
//        Coupon coupon = stripeService.retriveCoupon(code);
//
//        if (coupon != null && coupon.getValid()) {
//            String details = (coupon.getPercentOff() == null ? "$" + (coupon.getAmountOff() / 100)
//                    : coupon.getPercentOff() + "%") + "OFF" + coupon.getDuration();
//            return new Response(true, details);
//        }
//        return new Response(false, "This coupon code is not available. This may be because it has expired or has "
//                + "already been applied to your account.");
//    }
//
//    @PostMapping("/create-charge")
//    public @ResponseBody Response createCharge(String email, String token) {
//
//        if (token == null) {
//            return new Response(false, "Stripe payment token is missing. Please try again.");
//        }
//
//        String chargeId = stripeService.createCharge(email, token, 999); // $9.99
//
//        if (chargeId == null) {
//            return new Response(false, "An error occurred while trying to charge.");
//        }
//
//        // Indicate success and include the redirection URL
//        return new Response(true, "Success! Your charge ID is " + chargeId, "/");
//    }

}