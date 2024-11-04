package com.soccerapp.app.service;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;

import java.util.List;

public interface PlayerService {

    List<Player> getPlayersByUserId(Long userId);
}
