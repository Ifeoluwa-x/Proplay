package com.soccerapp.app.repository;

import com.soccerapp.app.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // Get Match by Location
    List<Match> findByLocation(String location);

    // Get Match by User ID
    List<Match> findByUserId(Long userId);

    // Get Match by Match ID
    Match findByMatchId(Long matchId);

    // Get Match by Team ID
    // Get Match by Team ID using derived query
//    List<Match> findByMatchParticipants_Team_TeamId(Long teamId);
}
