package com.soccerapp.app.repository;

import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
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


    // Additional custom queries can be added here as needed
}
