package com.soccerapp.app.service;

public interface MatchPartService {

    public void joinFriendly(Long teamId, Long playerId);

    int countParticipantsByMatchId(Long matchId);

    boolean hasPlayerJoinedMatch(Long matchId, Long playerId);
}
