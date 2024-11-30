package com.soccerapp.app.dto;

import com.soccerapp.app.models.Match;
import com.soccerapp.app.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchDto {

    private Long matchId;
    private LocalDateTime matchDate;
    private Match.MatchMode matchMode;
    private String location;
    private Match.MatchType type;
    private Integer maxPlayers;
    private UserDto user;


    private LocalDateTime date;
    private String formattedDate;
    private int availableSpots; // Remaining spots
    private boolean joinDisabled; // Whether the join button should be disabled
    private boolean hasPlayerJoined;



    // Getter and setter for date
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // Getter and setter for formattedDate
    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    public boolean isJoinDisabled() {
        return joinDisabled;
    }

    public void setJoinDisabled(boolean joinDisabled) {
        this.joinDisabled = joinDisabled;
    }

    public boolean isHasPlayerJoined() {
        return hasPlayerJoined;
    }

    public void setHasPlayerJoined(boolean hasPlayerJoined) {
        this.hasPlayerJoined = hasPlayerJoined;
    }

}