package com.soccerapp.app.mapper;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.User;

public class PlayerMapper {
    private Player mapToPlayer(PlayerDto playerDto) {
        return Player.builder()
                .availability(playerDto.getAvailability())
                .position(playerDto.getPosition())
                .skillLevel(playerDto.getSkillLevel())
                .playerBio(playerDto.getPlayerBio())
                .user(User.builder()
                        .fname(playerDto.getFname())
                        .lname(playerDto.getLname())
                        .email(playerDto.getEmail())
                        .age(playerDto.getAge())
                        .sex(playerDto.getSex())
                        .build())
//                .team(team) // Now passing the correct Team object
                .build();
    }
}
