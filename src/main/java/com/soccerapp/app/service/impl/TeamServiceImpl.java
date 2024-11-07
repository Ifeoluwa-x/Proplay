package com.soccerapp.app.service.impl;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.models.User;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.repository.UserRepository;
import com.soccerapp.app.service.TeamService;
import com.soccerapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private UserService userService; // Assuming UserService can retrieve User by ID

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;


    public TeamServiceImpl(PlayerRepository playerRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public TeamDto createTeam(TeamDto teamDto) {
        Team team = mapToTeam(teamDto);
        Team savedTeam = teamRepository.save(team);
        return mapToDto(savedTeam);

    }

    @Override // Implement the interface method
    public List<Team> getTeamByUserId(Long userId) {
        return teamRepository.findByUserId(userId);
    }










    private Team mapToTeam(TeamDto teamDto) {
        // Fetch owner (User) by ID
        User owner = userRepository.findById(teamDto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch team captain and vice-captain by their IDs, if present
        Player captain = (teamDto.getTeamCaptainId() != null)
                ? playerRepository.findById(teamDto.getTeamCaptainId())
                .orElseThrow(() -> new RuntimeException("Captain not found"))
                : null;

        Player viceCaptain = (teamDto.getTeamViceId() != null)
                ? playerRepository.findById(teamDto.getTeamViceId())
                .orElseThrow(() -> new RuntimeException("Vice-Captain not found"))
                : null;

        // Fetch list of players if player IDs are provided
        List<Player> players = (teamDto.getPlayerIds() != null)
                ? playerRepository.findAllById(teamDto.getPlayerIds())
                : new ArrayList<>();

        // Map TeamDto to Team
        return Team.builder()
                .teamId(teamDto.getTeamId())
                .teamName(teamDto.getTeamName())
                .teamLogo(teamDto.getTeamLogo())
                .teamCaptain(captain)
                .teamVice(viceCaptain)
                .teamLocation(teamDto.getTeamLocation())
                .createdAt(teamDto.getCreatedAt())
                .teamDesc(teamDto.getTeamDesc())
                .user(owner)
                .players(players)
                .build();
    }




    private TeamDto mapToDto(Team team) {
        return TeamDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .teamLogo(team.getTeamLogo())
                .teamLocation(team.getTeamLocation())
                .createdAt(team.getCreatedAt())
                .teamDesc(team.getTeamDesc())
                .ownerId(team.getUser().getId()) // Assuming getId() returns the ID of the User

                // Set captain and vice-captain IDs if they are not null
                .teamCaptainId(team.getTeamCaptain() != null ? team.getTeamCaptain().getId() : null)
                .teamViceId(team.getTeamVice() != null ? team.getTeamVice().getId() : null)

                // Map the list of player IDs if the team has players
                .playerIds(team.getPlayers() != null
                        ? team.getPlayers().stream().map(Player::getId).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }



}
