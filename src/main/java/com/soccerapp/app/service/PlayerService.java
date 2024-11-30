package com.soccerapp.app.service;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    List<Player> getPlayersByUserId(Long userId);

    PlayerDto createPlayerProfile(PlayerDto playerDto);

    public void updatePlayer(Long id, PlayerDto playerDto);

    List<PlayerDto> findAllPlayers();

    List<PlayerDto> findPlayersNotInTeam(Long teamId);

//    public Player getPlayerById(Long playerId);

    public PlayerDto findByPlayerId(long id);

//    void addPlayerToTeam(Long playerId, Long teamId);
}
