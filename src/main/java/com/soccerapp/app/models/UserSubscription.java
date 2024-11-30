package com.soccerapp.app.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscription")
@Data
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSubscriptionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime nextBillingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active;



    // Getters and Setters

    public enum SubscriptionStatus {
        ACTIVE,
        CANCELED,
        EXPIRED
    }

    public boolean isActive() { // Ensure proper naming
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Additional fields for formatted dates
    private String formattedCreatedAt;
    private String formattedUpdatedAt;

    // Getters and setters for formatted dates
    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }

    public void setFormattedCreatedAt(String formattedCreatedAt) {
        this.formattedCreatedAt = formattedCreatedAt;
    }

    public String getFormattedUpdatedAt() {
        return formattedUpdatedAt;
    }

    public void setFormattedUpdatedAt(String formattedUpdatedAt) {
        this.formattedUpdatedAt = formattedUpdatedAt;
    }}

