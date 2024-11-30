package com.soccerapp.app.service.impl;

import com.soccerapp.app.dto.TeamDto;
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
import java.util.Comparator;
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

    public void addPlayerToTeam(Long teamId, Long playerId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found"));

        player.setTeam(team);
        playerRepository.save(player);  // Save the player to associate them with the team
    }

    public Team getTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Custom sorting based on position priority
        List<Player> sortedPlayers = team.getPlayers()
                .stream()
                .sorted(Comparator.comparingInt(player -> getPositionPriority(player.getPosition())))
                .collect(Collectors.toList());

        team.setPlayers(sortedPlayers); // Assuming your Team class has a setter for players
        return team;
    }

    // Method to assign priority to positions
    private int getPositionPriority(String position) {
        switch (position.toLowerCase()) {
            case "goalkeeper":
                return 1;
            case "defender":
                return 2;
            case "midfielder":
                return 3;
            case "attacker":
            case "forward":
                return 4;
            default:
                return 5; // Lowest priority for unknown positions
        }
    }


    public TeamDto findByTeamId(long id){
        Team team = teamRepository.findByTeamId(id).orElseThrow(() -> new RuntimeException("Team not found"));
        return mapToDto(team);
    }

    public List<TeamDto> getAllTeams() {
        // Retrieve all teams as Team entities
        List<Team> teams = teamRepository.findAll();

        // Convert each Team entity to TeamDto
        return teams.stream()
                .map(this::mapToDto)  // Call a helper method to convert each Team to TeamDto
                .collect(Collectors.toList());
    }

    public List<TeamDto> getTeamsByLocation(String location) {
        List<Team> teams = teamRepository.findByTeamLocationContainingIgnoreCase(location);

        // Manual mapping of Team to TeamDto
        List<TeamDto> teamDtos = new ArrayList<>();
        for (Team team : teams) {
            TeamDto dto = new TeamDto();
            dto.setTeamId(team.getTeamId());
            dto.setTeamName(team.getTeamName());
            dto.setTeamLocation(team.getTeamLocation());
            dto.setTeamDesc(team.getTeamDesc());
            dto.setOwnerId(team.getUser().getId());
            dto.setTeamLogo(team.getTeamLogo());
            // Add any other fields you want to include
            teamDtos.add(dto);
        }
        return teamDtos;
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
                .teamCaptain(captain) // Set captain (null if not provided)
                .teamVice(viceCaptain) // Set vice-captain (null if not provided)
                .teamLocation(teamDto.getTeamLocation())
                .createdAt(teamDto.getCreatedAt())
                .teamDesc(teamDto.getTeamDesc())
                .user(owner) // Owner (User)
                .players(players) // List of players
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
