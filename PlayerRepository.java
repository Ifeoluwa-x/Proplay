package com.soccerapp.app.repository;

import com.soccerapp.app.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findPlayerByPlayerId(Integer playerId);

    @Modifying
    @Query("UPDATE Player p SET p.availability = :availability, p.position = :position, p.skillLevel = :skillLevel," +
            " p.playerBio = :playerBio WHERE p.playerId = :playerId")
    void updatePlayerFields(@Param("playerId") Integer playerId,
                            @Param("availability") String availability,
                            @Param("position") String position,
                            @Param("skillLevel") String skillLevel,
                            @Param("playerBio") String playerBio);
}
