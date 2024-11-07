package com.soccerapp.app.service.impl;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Role;
import com.soccerapp.app.models.User;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.UserRepository;
import com.soccerapp.app.service.PlayerService;
import com.soccerapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService { // Implement the PlayerService interface


    @Autowired
    private UserService userService; // Assuming UserService can retrieve User by ID

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;


    public PlayerServiceImpl(PlayerRepository playerRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    @Override // Implement the interface method
    public List<Player> getPlayersByUserId(Long userId) {
        return playerRepository.findByUserId(userId);
    }


    public PlayerDto createPlayerProfile(PlayerDto playerDto) {
        Player player = mapToPlayer(playerDto);
        Player savedPlayer = playerRepository.save(player); // Ensures ID is populated
        return mapToDto(savedPlayer); // Returns DTO with populated ID
    }


    private Player mapToPlayer(PlayerDto playerDto) {
        // Fetch the User object from the database using the userId in PlayerDto
        User user = userRepository.findById(playerDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and return the Player entity using the data from PlayerDto
        return Player.builder()
                .id(playerDto.getPlayerId())
                .playerBio(playerDto.getPlayerBio())
                .position(playerDto.getPosition())
                .availability(playerDto.getAvailability())
                .skillLevel(playerDto.getSkillLevel())
                .user(user)  // Set the fetched User object
                .team(playerDto.getTeamId())
                .build();
    }

    private PlayerDto mapToDto(Player player) {
        return PlayerDto.builder()
                .playerId(player.getId())
                .availability(player.getAvailability())
                .position(player.getPosition())
                .skillLevel(player.getSkillLevel())
                .playerBio(player.getPlayerBio())
                .userId(player.getUser() != null ? player.getUser().getId() : null)
                .teamId(player.getTeam())
                .build();
    }
}