package com.soccerapp.app.repository;

import com.soccerapp.app.models.PlayerRequest;
import com.soccerapp.app.models.TeamRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRequestRepository  extends JpaRepository<PlayerRequest, Long> {

    // Find requests by player ID that are unread
    List<PlayerRequest> findByTeam_TeamIdAndIsRead(Long teamId, Boolean isRead);

    // Find all requests by a specific team
//    List<TeamRequest> findByTeam_TeamId(Long teamId);

    // Find requests with a specific decision status
    List<PlayerRequest> findByDecision(String decision);

    // Find all requests for a specific player, ordered by createdAt
    List<PlayerRequest> findByTeam_TeamIdOrderByCreatedAtDesc(Long teamId);

    boolean existsByTeam_TeamIdAndPlayer_Id(Long teamId, Long playerId);

    List<PlayerRequest> findByTeam_TeamId(Long teamId);
}