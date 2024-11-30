package com.soccerapp.app.repository;

import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // Find team by its name
    Optional<Team> findByTeamName(String teamName);

    // Find teams by location
    List<Team> findByTeamLocation(String teamLocation);

    // Find all teams created by a specific user (team owner)
    List<Team> findByUserId(Long ownerId);

    Optional<Team> findByTeamId(long id);

    List<Team> findByTeamLocationContainingIgnoreCase(String location);

    @Query("""
    SELECT p FROM Player p
    WHERE p.team.id = :teamId
    ORDER BY 
        CASE 
            WHEN p.position = 'Goalkeeper' THEN 1
            WHEN p.position = 'Defender' THEN 2
            WHEN p.position = 'Midfielder' THEN 3
            WHEN p.position = 'Attacker' OR p.position = 'Forward' THEN 4
            ELSE 5
        END
""")
    List<Player> findPlayersByTeamIdOrderByPosition(@Param("teamId") Long teamId);
    // Additional custom queries can be added here as needed
}
