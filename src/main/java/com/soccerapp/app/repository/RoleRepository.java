package com.soccerapp.app.repository;

import com.soccerapp.app.models.Role;
import com.soccerapp.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
