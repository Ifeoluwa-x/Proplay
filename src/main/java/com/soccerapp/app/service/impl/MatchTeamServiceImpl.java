package com.soccerapp.app.service.impl;

import com.soccerapp.app.models.*;
import com.soccerapp.app.repository.MatchRepository;
import com.soccerapp.app.repository.MatchTeamReqRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.service.MatchTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@Service
public class MatchTeamServiceImpl implements MatchTeamService {
    @Autowired
    private MatchTeamReqRepository matchTeamReqRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    // Example usage
    public List<MatchTeamReq> getRequestsByMatch(Long matchId) {
        return matchTeamReqRepository.findByMatch_matchId(matchId);
    }

    public List<MatchTeamReq> getAwayTeamsRequests(Long awayTeamId) {
        return matchTeamReqRepository.findByAwayTeam_teamId(awayTeamId);
    }

    // Send a team request to a player
    public void sendRequest(Long matchId, Long homeTeamId, Long awayTeamId) {
        Team homeTeam = teamRepository.findById(homeTeamId)
                .orElseThrow(() -> new RuntimeException("No Home Team found with id " + homeTeamId));
        Team awayTeam = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new RuntimeException("No Away Team found with id " + awayTeamId));  // Fix the error message here
        Match match = matchRepository.findByMatchId(matchId);

        // Define the predefined custom message with actual details
        String predefinedMessage = "Hi " + awayTeam.getTeamName() + ",\n\n"
                + "We'd love to organize a friendly match with you on "
                + match.getMatchDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"))
                + " at " + match.getLocation() + ".\n\n"
                + "Let us know if this works or suggest an alternative. Looking forward to hearing from you!\n\n"
                + "--- " + homeTeam.getTeamName();

        // Create and set up the MatchTeamReq object
        MatchTeamReq matchTeamReq = new MatchTeamReq();
        matchTeamReq.setAwayTeam(awayTeam);
        matchTeamReq.setHomeTeam(homeTeam);
        matchTeamReq.setMatch(match);
        matchTeamReq.setDecision("Pending");
        matchTeamReq.setCreatedAt(LocalDateTime.now());
        matchTeamReq.setUpdatedAt(LocalDateTime.now());
        matchTeamReq.setRead(false);
        matchTeamReq.setCustomMessage(predefinedMessage);

        // Save the MatchTeamReq object to the repository
        matchTeamReqRepository.save(matchTeamReq);
    }

    // Find all match team requests where the team is in the awayTeam column and decision is "Pending"
    public List<MatchTeamReq> findPendingAwayTeamMatches(Long teamId) {
        return matchTeamReqRepository.findByAwayTeam_teamIdAndDecision(teamId, "Pending");
    }

    // Accept a team friendLy Request
    public void acceptTeamMatchRequest(Long MatchReqId) {
        MatchTeamReq request = matchTeamReqRepository.findById(MatchReqId)
                .orElseThrow(() -> new RuntimeException("Request not found with id " + MatchReqId));

        request.setDecision("ACCEPTED");
        request.setRead(true);
        matchTeamReqRepository.save(request);
    }

    public List<MatchTeamReq> findHomeTeamMatchesByStatus(Long teamId, String... statuses) {
        return matchTeamReqRepository.findByHomeTeam_teamIdAndDecisionIn(teamId, Arrays.asList(statuses));
    }

    public List<MatchTeamReq> findAcceptedMatchRequests(Long teamId) {
        return matchTeamReqRepository.findByDecisionAndHomeTeam_teamIdOrDecisionAndAwayTeam_teamId("ACCEPTED", teamId, "ACCEPTED", teamId);
    }

}
