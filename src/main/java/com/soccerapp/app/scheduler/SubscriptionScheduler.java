package com.soccerapp.app.scheduler;

import com.soccerapp.app.models.UserSubscription;
import com.soccerapp.app.repository.UserSubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SubscriptionScheduler {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Transactional
    @Scheduled(cron = "0 55 12 * * *") // Runs every day at 12:55 PM
    public void checkAndExpireSubscriptions() {
        LocalDateTime now = LocalDateTime.now();

        // Find subscriptions where the NextBillingDate has passed and status is ACTIVE
        List<UserSubscription> subscriptions = userSubscriptionRepository.findByNextBillingDateBeforeAndStatus(
                now, UserSubscription.SubscriptionStatus.ACTIVE
        );

        // Update status to EXPIRED for these subscriptions
        for (UserSubscription subscription : subscriptions) {
            subscription.setStatus(UserSubscription.SubscriptionStatus.EXPIRED);
            userSubscriptionRepository.save(subscription);
        }

        System.out.println("Checked and updated subscriptions at: " + now);
    }
}
