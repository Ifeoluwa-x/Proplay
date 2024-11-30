package com.soccerapp.app.service.impl;

import com.soccerapp.app.dto.SubscriptionPlanDto;
import com.soccerapp.app.models.SubscriptionPlan;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.repository.SubscriptionPlanRepository;
import com.soccerapp.app.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanServiceImpl  implements SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    public List<SubscriptionPlanDto> getAllPlans() {
        List<SubscriptionPlan> plans = subscriptionPlanRepository.findAll();
        return plans.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SubscriptionPlan getPlanById(Long planId) {
        return subscriptionPlanRepository.findById(planId).orElseThrow(() -> new RuntimeException("Team not found"));
    }

    // Convert SubscriptionPlan entity to SubscriptionPlanDto
    private SubscriptionPlanDto convertToDTO(SubscriptionPlan plan) {
        return new SubscriptionPlanDto(
                plan.getSubscriptionId(),
                plan.getPlanName(),
                plan.getCurrency(),
                plan.getDescription(),
                plan.getPrice(),
                plan.getBillingCycle(),
                plan.getCreatedAt()
        );
    }
}
