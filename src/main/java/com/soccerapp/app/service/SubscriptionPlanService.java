package com.soccerapp.app.service;

import com.soccerapp.app.dto.SubscriptionPlanDto;
import com.soccerapp.app.models.SubscriptionPlan;

import java.util.List;

public interface SubscriptionPlanService  {
    List<SubscriptionPlanDto> getAllPlans();

    public SubscriptionPlan getPlanById(Long planId);
}
