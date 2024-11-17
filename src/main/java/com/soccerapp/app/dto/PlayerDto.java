package com.soccerapp.app.dto;

import com.soccerapp.app.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {
    private Long playerId;
    private String availability;
    private String position;
    private String skillLevel;
    private String playerBio;
    private TeamDto team;
    private UserDto user;

    private boolean requestSent;  // Add this field

    // Getters and setters
    public boolean isRequestSent() {
        return requestSent;
    }

    public void setRequestSent(boolean requestSent) {
        this.requestSent = requestSent;
    }
}
