package com.soccerapp.app.service;

import com.soccerapp.app.models.MatchTeamReq;

import java.util.List;

public interface MatchTeamService {

    List<MatchTeamReq> getRequestsByMatch(Long matchId);

    List<MatchTeamReq> getAwayTeamsRequests(Long awayTeamId);

    void sendRequest(Long matchId, Long homeTeamId, Long awayTeamId);

    List<MatchTeamReq> findPendingAwayTeamMatches(Long teamId);

    void acceptTeamMatchRequest(Long MatchReqId);

    List<MatchTeamReq> findHomeTeamMatchesByStatus(Long teamId, String... statuses);

    List<MatchTeamReq> findAcceptedMatchRequests(Long teamId);
}
