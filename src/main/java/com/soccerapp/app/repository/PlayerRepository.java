package com.soccerapp.app.repository;

import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByUserId(Long userId);

    Optional<Player> findById(long id);
}


