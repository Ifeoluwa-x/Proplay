package com.soccerapp.app.service;


import com.soccerapp.app.dto.PlayerDto;

import java.util.List;

public interface PlayerService {
    PlayerDto createPlayer(PlayerDto playerDto);

    List<PlayerDto> findAllPlayers();

    void deletePlayer(Integer playerId);

    PlayerDto findPlayerById(Integer playerId);

    void updatePlayerFields(PlayerDto playerDto);
}
