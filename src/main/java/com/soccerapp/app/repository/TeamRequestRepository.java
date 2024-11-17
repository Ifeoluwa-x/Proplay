package com.soccerapp.app.repository;

import com.soccerapp.app.models.TeamRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long> {

    // Find requests by player ID that are unread
    List<TeamRequest> findByPlayerIdAndIsRead(Long playerId, Boolean isRead);

    // Find all requests by a specific team
//    List<TeamRequest> findByTeam_TeamId(Long teamId);

    // Find requests with a specific decision status
//    List<TeamRequest> findByDecision(String decision);

    // Find all requests for a specific player, ordered by createdAt
    List<TeamRequest> findByPlayerIdOrderByCreatedAtDesc(Long playerId);

    boolean existsByTeam_TeamIdAndPlayer_Id(Long teamId, Long playerId);

    List<TeamRequest> findByPlayer_Id(Long playerId);
}
