package com.soccerapp.app.service.impl;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Role;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.models.User;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.repository.UserRepository;
import com.soccerapp.app.service.PlayerService;
import com.soccerapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Player> getPlayersByUserId(Long userId) {
        return playerRepository.findByUserId(userId);
    }

    public PlayerDto findByPlayerId(long id) {
        return playerRepository.findById(id)
                .map(this::mapToDto) // Only map if the user is found
                .orElse(null); // Return null if no user is found
    }

    public PlayerDto createPlayerProfile(PlayerDto playerDto) {
        Player player = mapToPlayer(playerDto);
        Player savedPlayer = playerRepository.save(player); // Ensures ID is populated
        return mapToDto(savedPlayer); // Returns DTO with populated ID
    }

    public List<PlayerDto> findAllPlayers() {
        List<Player> players = playerRepository.findAll();
        return players.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<PlayerDto> findPlayersNotInTeam(Long teamId) {
        List<Player> players = playerRepository.findAll();  // Get all players from the database

        // Filter players who do not belong to the team with the given teamId
        return players.stream()
                .filter(player -> player.getTeam() == null || !player.getTeam().getTeamId().equals(teamId))  // Check if player is not in the given team
                .map(this::mapToDto)  // Convert Player to PlayerDto
                .collect(Collectors.toList());  // Collect filtered players as a list of PlayerDto
    }

    // Method to assign a player to a team
    public void addPlayerToTeam(Long playerId, Long teamId) {
        // Retrieve the player from the database
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Retrieve the team from the database
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Set the team to the player
        player.setTeam(team);

        // Save the updated player
        playerRepository.save(player);
    }

    private Player mapToPlayer(PlayerDto playerDto) {
        User user = userRepository.findById(playerDto.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team team = teamRepository.findById(playerDto.getTeam().getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        return Player.builder()
                .id(playerDto.getPlayerId())
                .playerBio(playerDto.getPlayerBio())
                .position(playerDto.getPosition())
                .availability(playerDto.getAvailability())
                .skillLevel(playerDto.getSkillLevel())
                .user(user)
                .team(team)
                .build();
    }

    private PlayerDto mapToDto(Player player) {
        UserDto userDto = null;
        if (player.getUser() != null) {
            userDto = UserDto.builder()
                    .id(player.getUser().getId())
                    .fname(player.getUser().getFname())
                    .lname(player.getUser().getLname())
                    .email(player.getUser().getEmail())
                    .age(player.getUser().getAge())
                    .location(player.getUser().getLocation())
                    .sex(player.getUser().getSex())
                    .build();
        }

        TeamDto teamDto = null;
        if (player.getTeam() != null) {
            teamDto = TeamDto.builder()
                    .teamId(player.getTeam().getTeamId())
                    .teamName(player.getTeam().getTeamName())
                    .teamLogo(player.getTeam().getTeamLogo())
                    .teamLocation(player.getTeam().getTeamLocation())
                    .teamDesc(player.getTeam().getTeamDesc())
                    .build();
        }

        return PlayerDto.builder()
                .playerId(player.getId())
                .availability(player.getAvailability())
                .position(player.getPosition())
                .skillLevel(player.getSkillLevel())
                .playerBio(player.getPlayerBio())
                .user(userDto)
                .team(teamDto)
                .build();
    }
}
