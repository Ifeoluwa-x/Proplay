package com.soccerapp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDto {
    private Long teamId;
    private String teamName;
    private String teamLogo;
    private Long teamCaptainId;
    private Long teamViceId;
    private String teamLocation;
    private LocalDateTime createdAt;
    private String teamDesc;
    private Long ownerId;
    private List<Long> playerIds;
}