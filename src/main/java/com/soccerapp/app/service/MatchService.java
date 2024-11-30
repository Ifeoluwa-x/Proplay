package com.soccerapp.app.service;

import com.soccerapp.app.dto.MatchDto;

import java.util.List;

public interface MatchService {
    MatchDto createMatch(MatchDto matchDto);

    List<MatchDto> findAllMatches();

    MatchDto createTeamMatch(MatchDto matchDto);

    List<MatchDto> getMatchesByLocation(String location);
}
