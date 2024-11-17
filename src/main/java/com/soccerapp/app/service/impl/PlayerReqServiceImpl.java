package com.soccerapp.app.service.impl;

import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.PlayerRequest;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.models.TeamRequest;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.PlayerRequestRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.repository.TeamRequestRepository;
import com.soccerapp.app.service.PlayerReqService;
import com.soccerapp.app.service.TeamReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlayerReqServiceImpl implements PlayerReqService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerRequestRepository playerRequestRepository;

    // Send a team request to a player
    public void sendRequest(Long teamId, Long playerId) {
        // Fetch the Team and Player objects from the database
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id " + teamId));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id " + playerId));

        // Define the predefined custom message
        String predefinedMessage = "Hello " + team.getTeamName() + "'s admin, I'd like to join your team. Kindly check out my player profile and bio";

        // Create and set up the TeamRequest object
        PlayerRequest playerRequest = new PlayerRequest();
        playerRequest.setTeam(team);  // Set the actual Team object
        playerRequest.setPlayer(player);  // Set the actual Player object
        playerRequest.setCustomMessage(predefinedMessage);  // Use the predefined custom message
        playerRequest.setCreatedAt(LocalDateTime.now());
        playerRequest.setUpdatedAt(LocalDateTime.now());
        playerRequest.setDecision("pending");

        // Save the team request to the database
        playerRequestRepository.save(playerRequest);
    }


    // Fetch team requests for a specific player
    public List<PlayerRequest> getRequestsForTeam(Long teamId) {
        return playerRequestRepository.findByTeam_TeamId(teamId);
    }

    // Accept a team request
    public void acceptRequest(Long requestId) {
        PlayerRequest request = playerRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id " + requestId));

        request.setDecision("ACCEPTED");
        request.setRead(true);
        playerRequestRepository.save(request);
    }

    // Decline a team request
    public void declineRequest(Long requestId) {
        PlayerRequest request = playerRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id " + requestId));

        request.setDecision("DECLINED");
        request.setRead(true);
        playerRequestRepository.save(request);
    }





















    // Retrieve unread requests for a player
    public List<PlayerRequest> getUnreadRequests(Long teamId) {
        return playerRequestRepository.findByTeam_TeamIdAndIsRead(teamId, false);
    }

    // Mark a request as read
    public void markAsRead(Long requestId) {
        PlayerRequest request = playerRequestRepository.findById(requestId).orElse(null);
        if (request != null) {
            request.setRead(true);
            request.setUpdatedAt(LocalDateTime.now());
            playerRequestRepository.save(request);
        }
    }

    // Accept or decline a team request
    public void makeDecision(Long requestId, String decision) {
        PlayerRequest request = playerRequestRepository.findById(requestId).orElse(null);
        if (request != null) {
            request.setDecision(decision);  // 'accept' or 'decline'
            request.setUpdatedAt(LocalDateTime.now());
            playerRequestRepository.save(request);
        }
    }

    // Get all requests for a specific player
    public List<PlayerRequest> getRequestsByPlayer(Long teamId) {
        return playerRequestRepository.findByTeam_TeamIdOrderByCreatedAtDesc(teamId);//findByPlayerIdOrderByCreatedAtDesc(playerId);
    }

    public boolean isRequestSent(Long teamId, Long playerId) {
        // Check if a team request already exists with the specified teamId and playerId
        return playerRequestRepository.existsByTeam_TeamIdAndPlayer_Id(teamId, playerId);
    }


    // Get all requests for a specific team
//    public List<TeamRequest> getRequestsByTeam(Long teamId) {
//        return teamRequestRepository.findByTeamId(teamId);
//    }
}