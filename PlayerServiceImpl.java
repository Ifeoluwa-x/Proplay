package com.soccerapp.app.service.impl;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.exception.ResourceNotFoundException;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public PlayerDto createPlayer(PlayerDto playerDto) {
        Player player = mapToPlayer(playerDto);
        Player savedPlayer = playerRepository.save(player);
        return mapToPlayerDto(savedPlayer);
    }

    @Override
    public List<PlayerDto> findAllPlayers() {
        List<Player> players = playerRepository.findAll();
        return players.stream().map(this::mapToPlayerDto).collect(Collectors.toList());
    }

    @Override
    public void deletePlayer(Integer playerId) {
        playerRepository.deleteById(playerId);
    }

    @Override
    public PlayerDto findPlayerById(Integer playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));
        return mapToPlayerDto(player);
    }

    @Override
    @Transactional
    public void updatePlayerFields(PlayerDto playerDto) {
        playerRepository.updatePlayerFields(
                playerDto.getPlayerId(),
                playerDto.getAvailability(),
                playerDto.getPosition(),
                playerDto.getSkillLevel(),
                playerDto.getPlayerBio()
        );
    }

    private Player mapToPlayer(PlayerDto playerDto) {
        Player player = new Player();
        player.setPlayerId(playerDto.getPlayerId());
        player.setAvailability(playerDto.getAvailability());
        player.setPosition(playerDto.getPosition());
        player.setSkillLevel(playerDto.getSkillLevel());
        player.setPlayerBio(playerDto.getPlayerBio());
        return player;
    }

    private PlayerDto mapToPlayerDto(Player player) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId(player.getPlayerId());
        playerDto.setAvailability(player.getAvailability());
        playerDto.setPosition(player.getPosition());
        playerDto.setSkillLevel(player.getSkillLevel());
        playerDto.setPlayerBio(player.getPlayerBio());
        return playerDto;
    }
}



