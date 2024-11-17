package com.soccerapp.app.models;




import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data

public class TeamRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")  // This column is the foreign key to the Team entity
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id")  // This column is the foreign key to the Player entity
    private Player player;

    private String customMessage;
    private String decision;
    private boolean isRead;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
}

