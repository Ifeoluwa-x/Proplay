package com.soccerapp.app.dto;

import com.soccerapp.app.models.SubscriptionPlan;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SubscriptionPlanDto {

    private Long subscriptionId;
    private String planName;
    private String description;
    private String currency;

    private Long price;
    private SubscriptionPlan.BillingCycle billingCycle;
    private LocalDateTime createdAt;


    // Constructor with all fields
    public SubscriptionPlanDto(Long subscriptionId, String planName, String currency, String description,
                               Long price, SubscriptionPlan.BillingCycle billingCycle, LocalDateTime createdAt) {
        this.subscriptionId = subscriptionId;
        this.planName = planName;
        this.currency = currency;
        this.description = description;
        this.price = price;
        this.billingCycle = billingCycle;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public SubscriptionPlan.BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(SubscriptionPlan.BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
