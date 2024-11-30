package com.soccerapp.app.repository;

import com.soccerapp.app.models.Match;
import com.soccerapp.app.models.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
    // Find all participants by match ID
    List<MatchParticipant> findByMatch_MatchId(Long matchId);

    // Find all participants by player ID (if player is not null)
    List<MatchParticipant> findByPlayer_Id(Long playerId);

    // Find all participants by team ID (if team is not null)
    List<MatchParticipant> findByTeam_TeamId(Long teamId);

    // Find a specific participant by match ID, player ID, and team ID
    Optional<MatchParticipant> findByMatch_MatchIdAndPlayer_IdAndTeam_TeamId(Long matchId, Long playerId, Long teamId);

    int countParticipantsByMatch_matchId(Long matchId);

    boolean existsByMatch_matchIdAndPlayerId(Long matchId, Long playerId);
}
