package com.soccerapp.app.repository;

import com.soccerapp.app.models.MatchTeamReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchTeamReqRepository extends JpaRepository<MatchTeamReq, Long> {

    // Custom query methods based on MatchTeamReq fields

    // Find all requests by a specific match
    List<MatchTeamReq> findByMatch_matchId(Long matchId);

    // Find all requests for a specific home team
    List<MatchTeamReq> findByHomeTeam_teamId(Long homeTeamId);

    // Find all requests for a specific away team
    List<MatchTeamReq> findByAwayTeam_teamId(Long teamId);

    // Find all requests by a specific decision status
    List<MatchTeamReq> findByDecision(String decision);

    // Find unread requests
    List<MatchTeamReq> findByIsReadFalse();

    //Find ExistingRequest by Home Team
    List<MatchTeamReq> findByHomeTeam_teamIdAndAwayTeam_teamIdAndDecision(Long homeTeamId, Long awayTeamId, String decision);

    // Find requests by custom message content (example for search functionality)
    List<MatchTeamReq> findByCustomMessageContainingIgnoreCase(String keyword);

    List<MatchTeamReq> findByAwayTeam_teamIdAndDecision(Long awayTeamId, String decision);

    List<MatchTeamReq> findByHomeTeam_teamIdAndDecisionIn(Long teamId, List<String> decisions);

    // Find by decision and where teamId matches homeTeam or awayTeam
    List<MatchTeamReq> findByDecisionAndHomeTeam_teamIdOrDecisionAndAwayTeam_teamId(String decision1,
                                                                          Long homeTeamId, String decision2,
                                                                          Long awayTeamId);

}
