package com.soccerapp.app;

import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.models.User;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.repository.UserRepository;
import com.soccerapp.app.service.UserService;
import com.soccerapp.app.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeamServiceImplTest {

    @InjectMocks
    private TeamServiceImpl teamService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTeam() {
        TeamDto teamDto = TeamDto.builder()
                .teamName("Team A")
                .teamLocation("Location A")
                .ownerId(1L)
                .build();

        User user = new User();
        user.setId(1L);

        Team team = Team.builder()
                .teamName("Team A")
                .teamLocation("Location A")
                .user(user)
                .build();

        Team savedTeam = Team.builder()
                .teamId(1L)
                .teamName("Team A")
                .teamLocation("Location A")
                .user(user)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.save(any(Team.class))).thenReturn(savedTeam);

        TeamDto result = teamService.createTeam(teamDto);

        assertNotNull(result);
        assertEquals("Team A", result.getTeamName());
        assertEquals("Location A", result.getTeamLocation());
        assertEquals(1L, result.getOwnerId());
    }

    @Test
    void testGetTeamByUserId() {
        User user = new User();
        user.setId(1L);

        Team team = new Team();
        team.setTeamId(1L);
        team.setTeamName("Team A");
        team.setUser(user);

        List<Team> teams = List.of(team);

        when(teamRepository.findByUserId(1L)).thenReturn(teams);

        List<Team> result = teamService.getTeamByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Team A", result.get(0).getTeamName());
    }

//    @Test
//    void testAddPlayerToTeam() {
//        Team team = new Team();
//        team.setTeamId(1L);
//
//        Player player = new Player();
//        player.setId(1L);
//
//        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
//        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
//        when(playerRepository.save(any(Player.class))).thenReturn(player);
//
//        teamService.addPlayerToTeam(1L, 1L);
//
//        verify(playerRepository, times(1)).save(player);
//        assertEquals(team, player.getTeam());
//    }

    @Test
    void testGetTeamById() {
        Team team = new Team();
        team.setTeamId(1L);
        team.setTeamName("Team A");

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Team result = teamService.getTeamById(1L);

        assertNotNull(result);
        assertEquals("Team A", result.getTeamName());
    }

    @Test
    void testFindByTeamId() {
        Team team = new Team();
        team.setTeamId(1L);
        team.setTeamName("Team A");

        User user = new User();
        user.setId(1L);

        team.setUser(user); // Mock the user for the team

        when(teamRepository.findByTeamId(1L)).thenReturn(Optional.of(team));

        TeamDto result = teamService.findByTeamId(1L);

        assertNotNull(result);
        assertEquals("Team A", result.getTeamName());
        assertEquals(1L, result.getOwnerId());
    }


    @Test
    void testGetAllTeams() {
        Team team1 = new Team();
        team1.setTeamId(1L);
        team1.setTeamName("Team A");

        Team team2 = new Team();
        team2.setTeamId(2L);
        team2.setTeamName("Team B");

        User user = new User();
        user.setId(1L);

        team1.setUser(user); // Mock the user for the team
        team2.setUser(user); //
        when(teamRepository.findAll()).thenReturn(List.of(team1, team2));

        List<TeamDto> result = teamService.getAllTeams();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Team A", result.get(0).getTeamName());
        assertEquals("Team B", result.get(1).getTeamName());
    }

    @Test
    void testGetTeamsByLocation() {
        Team team = new Team();
        team.setTeamId(1L);
        team.setTeamName("Team A");
        team.setTeamLocation("Location A");
        team.setUser(new User());


        when(teamRepository.findByTeamLocationContainingIgnoreCase("Location A")).thenReturn(List.of(team));

        List<TeamDto> result = teamService.getTeamsByLocation("Location A");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Team A", result.get(0).getTeamName());
        assertEquals("Location A", result.get(0).getTeamLocation());
    }
}
