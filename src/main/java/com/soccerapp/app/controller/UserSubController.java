package com.soccerapp.app.controller;

import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.SubscriptionPlan;
import com.soccerapp.app.models.User;
import com.soccerapp.app.models.UserSubscription;
import com.soccerapp.app.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stripe.model.Coupon;
import com.soccerapp.app.utils.Response;
import org.springframework.web.servlet.view.RedirectView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.soccerapp.app.utils.DateUtil.getDayWithSuffix;

@Controller
public class UserSubController {

    private UserSubscriptionService userSubscriptionService;
    private SubscriptionPlanService subscriptionPlanService;
    private UserService userService;

    public UserSubController(UserSubscriptionService userSubscriptionService,
                             SubscriptionPlanService subscriptionPlanService,
                             UserService userService) {
        this.userSubscriptionService = userSubscriptionService;
        this.subscriptionPlanService = subscriptionPlanService;
        this.userService = userService;
    }

    @GetMapping("/create/user/subscription{userId}/{subscriptionId}")
    public String createUserSub(@PathVariable Long userId,
                                 @PathVariable Long subscriptionId,
                                 Model model) {
        // Fetch the userDto (assuming the service returns a UserDto)
        UserDto userDto = userService.findUserById(userId); // This is likely where the issue originates

        // Convert UserDto to User using the mapToUser method
        User user = userService.mapToUser(userDto);  // Use the mapToUser method
        SubscriptionPlan selectedPlan = subscriptionPlanService.getPlanById(subscriptionId);

        userSubscriptionService.createUserSubscription(user, selectedPlan);
        return "redirect:/profile";
    }

    @GetMapping("/subscription-history")
    public String showSubscriptionHistory(Model model, HttpSession session) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        Long userId = loggedInUser.getId();

        // Fetch subscription history for the logged-in user
        List<UserSubscription> subscriptions = userSubscriptionService.getSubscriptionsByUser(userId);

        // Create a DateTimeFormatter for the full month name
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM, yyyy");


        // Format createdAt and updatedAt for each subscription
        for (UserSubscription subscription : subscriptions) {
            // Format the startDate and nextBillingDate fields
            String formattedCreatedAt = subscription.getStartDate() != null
                    ? getDayWithSuffix(subscription.getStartDate().getDayOfMonth()) + " " + subscription.getStartDate().format(monthYearFormatter)
                    : null;

            String formattedUpdatedAt = subscription.getNextBillingDate() != null
                    ? getDayWithSuffix(subscription.getNextBillingDate().getDayOfMonth()) + " " + subscription.getNextBillingDate().format(monthYearFormatter)
                    : null;

            // Store the formatted dates in new fields (do not set them back into startDate or nextBillingDate)
            subscription.setFormattedCreatedAt(formattedCreatedAt);
            subscription.setFormattedUpdatedAt(formattedUpdatedAt);
        }


        // Add the subscriptions to the model
        model.addAttribute("user", loggedInUser);


        model.addAttribute("subscriptions", subscriptions);
        return "subscription_history"; // Name of the Thymeleaf template
    }

}