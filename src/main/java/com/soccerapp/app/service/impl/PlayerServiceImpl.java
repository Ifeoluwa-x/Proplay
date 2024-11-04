package com.soccerapp.app.service.impl;

import com.soccerapp.app.models.Player;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.service.PlayerService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService { // Implement the PlayerService interface

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override // Implement the interface method
    public List<Player> getPlayersByUserId(Long userId) {
        return playerRepository.findByUserId(userId);
    }
}