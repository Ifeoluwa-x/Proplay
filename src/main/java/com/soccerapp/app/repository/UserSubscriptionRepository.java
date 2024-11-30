package com.soccerapp.app.repository;

import com.soccerapp.app.models.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    // You can define custom query methods if needed. For example:

    // Find a UserSubscription by user ID
    List<UserSubscription> findByUserId(Long userId);

    // Find all active subscriptions for a user
    List<UserSubscription> findByUserIdAndStatus(Long userId, UserSubscription.SubscriptionStatus status);
//
//    // Find all subscriptions by status
//    List<UserSubscription> findByStatus(UserSubscription.SubscriptionStatus status);
//
//    // Find subscriptions by subscription plan
//    List<UserSubscription> findBySubscriptionPlanId(Long subscriptionPlanId);

    // You can add more query methods as per your requirements.

    List<UserSubscription> findByNextBillingDateBeforeAndStatus(LocalDateTime dateTime, UserSubscription.SubscriptionStatus status);

}
