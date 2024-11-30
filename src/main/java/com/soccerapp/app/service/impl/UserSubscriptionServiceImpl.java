package com.soccerapp.app.service.impl;

import com.soccerapp.app.models.UserSubscription;
import com.soccerapp.app.models.User;
import com.soccerapp.app.models.SubscriptionPlan;
import com.soccerapp.app.repository.UserSubscriptionRepository;
import com.soccerapp.app.service.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    public UserSubscriptionServiceImpl(UserSubscriptionRepository userSubscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Override
    public UserSubscription createUserSubscription(User user, SubscriptionPlan subscriptionPlan) {
        // Create a new UserSubscription instance
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscriptionPlan(subscriptionPlan);
        userSubscription.setStartDate(LocalDateTime.now());
        userSubscription.setNextBillingDate(LocalDateTime.now().plusMonths(1L));
        userSubscription.setStatus(UserSubscription.SubscriptionStatus.ACTIVE); // Default to active status

        // Save the user subscription to the database
        return userSubscriptionRepository.save(userSubscription);
    }

    @Override
    public List<UserSubscription> getSubscriptionsByUser(Long userId) {
        return userSubscriptionRepository.findByUserId(userId);
    }

    public boolean hasActiveSubscription(Long userId) {
        List<UserSubscription> activeSubscriptions = userSubscriptionRepository.findByUserIdAndStatus(userId, UserSubscription.SubscriptionStatus.ACTIVE);
        return !activeSubscriptions.isEmpty(); // Check if there is at least one active subscription
    }

}
