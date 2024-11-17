package com.soccerapp.app.service.impl;

import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.models.TeamRequest;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.repository.TeamRequestRepository;
import com.soccerapp.app.service.TeamReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeamReqServiceImpl implements TeamReqService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRequestRepository teamRequestRepository;

    // Send a team request to a player
    public void sendRequest(Long teamId, Long playerId) {
        // Fetch the Team and Player objects from the database
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id " + teamId));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id " + playerId));

        // Define the predefined custom message
        String predefinedMessage = "Hello " + player.getUser().getFname() + ", we would like to invite you to join our team, "
                + team.getTeamName() + ". Please review the request and let us know your decision.";

        // Create and set up the TeamRequest object
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setTeam(team);  // Set the actual Team object
        teamRequest.setPlayer(player);  // Set the actual Player object
        teamRequest.setCustomMessage(predefinedMessage);  // Use the predefined custom message
        teamRequest.setCreatedAt(LocalDateTime.now());
        teamRequest.setUpdatedAt(LocalDateTime.now());
        teamRequest.setDecision("pending");

        // Save the team request to the database
        teamRequestRepository.save(teamRequest);
    }


    // Fetch team requests for a specific player
    public List<TeamRequest> getRequestsForPlayer(Long playerId) {
        return teamRequestRepository.findByPlayer_Id(playerId);
    }

    // Accept a team request
    public void acceptRequest(Long requestId) {
        TeamRequest request = teamRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id " + requestId));

        request.setDecision("ACCEPTED");
        request.setRead(true);
        teamRequestRepository.save(request);
    }

    // Decline a team request
    public void declineRequest(Long requestId) {
        TeamRequest request = teamRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id " + requestId));

        request.setDecision("DECLINED");
        request.setRead(true);
        teamRequestRepository.save(request);
    }





















    // Retrieve unread requests for a player
    public List<TeamRequest> getUnreadRequests(Long playerId) {
        return teamRequestRepository.findByPlayerIdAndIsRead(playerId, false);
    }

    // Mark a request as read
    public void markAsRead(Long requestId) {
        TeamRequest teamRequest = teamRequestRepository.findById(requestId).orElse(null);
        if (teamRequest != null) {
            teamRequest.setRead(true);
            teamRequest.setUpdatedAt(LocalDateTime.now());
            teamRequestRepository.save(teamRequest);
        }
    }

    // Accept or decline a team request
    public void makeDecision(Long requestId, String decision) {
        TeamRequest teamRequest = teamRequestRepository.findById(requestId).orElse(null);
        if (teamRequest != null) {
            teamRequest.setDecision(decision);  // 'accept' or 'decline'
            teamRequest.setUpdatedAt(LocalDateTime.now());
            teamRequestRepository.save(teamRequest);
        }
    }

    // Get all requests for a specific player
    public List<TeamRequest> getRequestsByPlayer(Long playerId) {
        return teamRequestRepository.findByPlayerIdOrderByCreatedAtDesc(playerId);
    }

    public boolean isRequestSent(Long teamId, Long playerId) {
        // Check if a team request already exists with the specified teamId and playerId
        return teamRequestRepository.existsByTeam_TeamIdAndPlayer_Id(teamId, playerId);
    }


    // Get all requests for a specific team
//    public List<TeamRequest> getRequestsByTeam(Long teamId) {
//        return teamRequestRepository.findByTeamId(teamId);
//    }
}
