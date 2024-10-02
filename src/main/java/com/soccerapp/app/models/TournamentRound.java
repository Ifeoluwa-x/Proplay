package com.soccerapp.app.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tournament_round")
public class TournamentRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roundId;

    @Column(nullable = false)
    private String roundName;

    // Getters and setters
}
