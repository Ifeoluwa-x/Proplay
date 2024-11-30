package com.soccerapp.app.service;

import com.soccerapp.app.models.UserSubscription;
import com.soccerapp.app.models.User;
import com.soccerapp.app.models.SubscriptionPlan;

import java.time.LocalDateTime;
import java.util.List;

public interface UserSubscriptionService {

    UserSubscription createUserSubscription(User user, SubscriptionPlan subscriptionPlan);

    List<UserSubscription> getSubscriptionsByUser(Long userId);

    boolean hasActiveSubscription(Long userId);
}
