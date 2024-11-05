package com.soccerapp.app.repository;

import com.soccerapp.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByFname(String currentName);

    Optional<User> findUserById(long id);

    @Modifying
    @Query("UPDATE User u SET u.email = :email, u.location = :location WHERE u.id = :id")
    void updateUserFields(@Param("id") Long id,
                          @Param("email") String email,
                          @Param("location") String location);
}
