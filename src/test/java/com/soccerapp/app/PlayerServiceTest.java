package com.soccerapp.app;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.models.User;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.repository.UserRepository;
import com.soccerapp.app.service.PlayerService;
import com.soccerapp.app.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private PlayerDto playerDto;
    private Player player;
    private User user;

    @BeforeEach
    void setUp() {
        // Set up sample user and player
        user = User.builder().id(1L).fname("John").lname("Doe").email("john@example.com").build();
        player = Player.builder().id(1L).user(user).playerBio("Great player").position("Forward").skillLevel("Advanced").build();
        playerDto = PlayerDto.builder().playerId(1L).playerBio("Great player").position("Forward").skillLevel("Advanced").build();
    }

//    @Test
//    void testCreatePlayerProfile() {
//        // Given
//        when(userRepository.findById(any())).thenReturn(Optional.of(user));
//        when(playerRepository.save(any(Player.class))).thenReturn(player);
//
//        // When
//        PlayerDto createdPlayer = playerService.createPlayerProfile(playerDto);
//
//        // Then
//        verify(playerRepository, times(1)).save(any(Player.class));
//        assert(createdPlayer.getPlayerId()).equals(player.getId());
//        assert(createdPlayer.getPlayerBio()).equals(player.getPlayerBio());
//    }

    @Test
    void testFindByPlayerId() {
        // Given
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));

        // When
        PlayerDto foundPlayer = playerService.findByPlayerId(1L);

        // Then
        assert(foundPlayer.getPlayerId()).equals(player.getId());
        assert(foundPlayer.getPlayerBio()).equals(player.getPlayerBio());
    }

    @Test
    void testFindAllPlayers() {
        // Given
        List<Player> players = Arrays.asList(player);
        when(playerRepository.findAll()).thenReturn(players);

        // When
        List<PlayerDto> playerDtos = playerService.findAllPlayers();

        // Then
        assert(playerDtos.size() == 1);
        assert(playerDtos.get(0).getPlayerId()).equals(player.getId());
    }

    @Test
    void testFindPlayersNotInTeam() {
        // Given
        List<Player> players = Arrays.asList(player);
        when(playerRepository.findAll()).thenReturn(players);

        // When
        List<PlayerDto> playersNotInTeam = playerService.findPlayersNotInTeam(2L);

        // Then
        assert(playersNotInTeam.size() == 1);
        assert(playersNotInTeam.get(0).getPlayerId()).equals(player.getId());
    }

    @Test
    void testAddPlayerToTeam() {
        // Given
        Long playerId = 1L;
        Long teamId = 1L;

        // Create a mock team object
        Team team = Team.builder()
                .teamId(teamId)
                .teamName("Test Team")
                .teamLogo("teamLogo.png")
                .teamLocation("Test Location")
                .teamDesc("Test Description")
                .build();

        // Create a mock player object
        Player player = Player.builder()
                .id(playerId)
                .user(user) // You can reuse the user object created earlier in your setup
                .playerBio("Great player")
                .position("Forward")
                .skillLevel("Advanced")
                .build();

        // Mock repository behavior
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // When
        playerService.addPlayerToTeam(playerId, teamId);

        // Then
        verify(playerRepository, times(1)).save(any(Player.class));
        assert(player.getTeam().getTeamId().equals(teamId));
    }


    @Test
    void testUpdatePlayer() {
        // Given
        Long playerId = 1L; // Use the correct player ID
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player)); // Make sure to use playerId in stubbing

        PlayerDto updatedPlayerDto = playerDto;
        updatedPlayerDto.setPlayerBio("Updated player bio");

        // When
        playerService.updatePlayer(playerId, updatedPlayerDto); // Use playerId here

        // Then
        verify(playerRepository, times(1)).save(any(Player.class)); // Verify save was called once
        assert(player.getPlayerBio().equals("Updated player bio"));
    }


    @Test
    void testGetPlayersByUserId() {
        // Given
        List<Player> players = Arrays.asList(player);
        when(playerRepository.findByUserId(anyLong())).thenReturn(players);

        // When
        List<Player> playerList = playerService.getPlayersByUserId(1L);

        // Then
        assert(playerList.size() == 1);
        assert(playerList.get(0).getId()).equals(player.getId());
    }
}
