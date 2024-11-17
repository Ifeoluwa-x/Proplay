package com.soccerapp.app.service;

import com.soccerapp.app.models.PlayerRequest;
import com.soccerapp.app.models.TeamRequest;

import java.util.List;

public interface PlayerReqService {

    public void sendRequest(Long teamId, Long playerId);

    public List<PlayerRequest> getRequestsForTeam(Long teamId);

    public void acceptRequest(Long requestId);

    public void declineRequest(Long requestId);

    public List<PlayerRequest> getUnreadRequests(Long teamId);

    public void markAsRead(Long requestId);

    public void makeDecision(Long requestId, String decision);

//    public List<PlayerRequest> getRequestsByTeam(Long teamId);

    public boolean isRequestSent(Long teamId, Long playerId);
}




