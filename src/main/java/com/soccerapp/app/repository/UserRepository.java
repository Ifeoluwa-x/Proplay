package com.soccerapp.app.repository;

import com.soccerapp.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(long id);

    @Modifying
    @Query("UPDATE User u SET u.position = :position, u.location = :location WHERE u.id = :id")
    void updateUserFields(@Param("id") Long id,
                          @Param("position") String position,
                          @Param("location") String location);
}
