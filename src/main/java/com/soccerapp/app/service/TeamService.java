package com.soccerapp.app.service;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface TeamService {
    TeamDto createTeam(TeamDto teamDto);

    List<Team> getTeamByUserId(Long userId);

    public void addPlayerToTeam(Long teamId, Long playerId);

    public Team getTeamById(Long teamId);

    public TeamDto findByTeamId(long id);

    public List<TeamDto> getAllTeams();
}
