package com.soccerapp.app.service.impl;

import com.soccerapp.app.models.*;
import com.soccerapp.app.repository.*;
import com.soccerapp.app.service.MatchPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MatchPartServiceImpl implements MatchPartService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRequestRepository teamRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private MatchParticipantRepository matchParticipantRepository;


    // Join a individual friendly match as a player
    public void joinFriendly(Long matchId, Long playerId) {
        // Fetch the Team and Player objects from the database

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id " + matchId));

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id " + playerId));



        // Create and set up the TeamRequest object
        MatchParticipant matchParticipant = new MatchParticipant();
        matchParticipant.setMatch(match); // Set the actual match object
        matchParticipant.setPlayer(player);  // Set the actual Player object

        // Save the team request to the database
        matchParticipantRepository.save(matchParticipant);
    }

//    // Join a individual friendly match as a player
//    public void createTeamFriendly(Long matchId, Long teamId) {
//        // Fetch the Team and Player objects from the database
//
//        Match match = matchRepository.findById(matchId)
//                .orElseThrow(() -> new RuntimeException("Match not found with id " + matchId));
//
////        User user = userRepository.findById(userId)
////                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
//
//        Player player = playerRepository.findById(playerId)
//                .orElseThrow(() -> new RuntimeException("Player not found with id " + playerId));
//
//
//
//        // Create and set up the TeamRequest object
//        MatchParticipant matchParticipant = new MatchParticipant();
//        matchParticipant.setMatch(match); // Set the actual match object
//        matchParticipant.setPlayer(player);  // Set the actual Player object
//
//        // Save the team request to the database
//        matchParticipantRepository.save(matchParticipant);
//    }


    public int countParticipantsByMatchId(Long matchId) {
        return matchParticipantRepository.countParticipantsByMatch_matchId(matchId);
    }
    public boolean hasPlayerJoinedMatch(Long matchId, Long playerId) {
        return matchParticipantRepository.existsByMatch_matchIdAndPlayerId(matchId, playerId);
    }
}
