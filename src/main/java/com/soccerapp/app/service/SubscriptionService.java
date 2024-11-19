package com.soccerapp.app.service;

import com.soccerapp.app.models.Subscription;

import java.util.List;

public interface SubscriptionService {
    Subscription createSubscription(Long userId, Subscription subscription);
    List<Subscription> getSubscriptionsByUser(Long userId);
    void cancelSubscription(Long subscriptionId);
}
