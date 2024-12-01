package com.soccerapp.app.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    private Integer playerId;
    private String availability;
    private String position;
    private String skillLevel;
    private String playerBio;
    private Integer userId;
    private Integer teamId;
}
