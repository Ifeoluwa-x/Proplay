package com.soccerapp.app.models;

import jakarta.persistence.*;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playerId;

    @Column(nullable = false)
    private String availability;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String skillLevel;

    @Column
    private String playerBio;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // Getters and setters
}
