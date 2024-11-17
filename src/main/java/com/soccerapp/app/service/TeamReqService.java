package com.soccerapp.app.service;

import com.soccerapp.app.models.TeamRequest;

import java.util.List;

public interface TeamReqService {

    public void sendRequest(Long teamId, Long playerId);

    public List<TeamRequest> getRequestsForPlayer(Long playerId);

    public void acceptRequest(Long requestId);

    public void declineRequest(Long requestId);

    public List<TeamRequest> getUnreadRequests(Long playerId);

    public void markAsRead(Long requestId);

    public void makeDecision(Long requestId, String decision);

    public List<TeamRequest> getRequestsByPlayer(Long playerId);

    public boolean isRequestSent(Long teamId, Long playerId);



//    public List<TeamRequest> getRequestsByTeam(Long teamId);
}
