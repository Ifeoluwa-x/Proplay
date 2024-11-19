package com.soccerapp.app;

import com.soccerapp.app.models.Subscription;
import com.soccerapp.app.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    void testCreateSubscription() {
        Subscription subscription = new Subscription();
        subscription.setPlanName("Premium");
        subscription.setPrice(new BigDecimal("9.99"));

        Subscription created = subscriptionService.createSubscription(1L, subscription);

        assertEquals("Premium", created.getPlanName());
    }
}

