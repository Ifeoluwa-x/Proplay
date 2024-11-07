package com.soccerapp.app.service;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;

import java.util.List;

public interface TeamService {
    TeamDto createTeam(TeamDto teamDto);

    List<Team> getTeamByUserId(Long userId);
}
