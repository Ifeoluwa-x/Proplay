//package com.soccerapp.app.models;
//
//import jakarta.persistence.*;
//import java.util.List;
//
//@Entity
//@Table(name = "match")
//public class Match {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer matchId;
//
//    @ManyToOne
//    @JoinColumn(name = "home_team_id", nullable = false)
//    private Team homeTeam;
//
//    @ManyToOne
//    @JoinColumn(name = "away_team_id", nullable = false)
//    private Team awayTeam;
//
//    @Column(nullable = false)
//    private Integer homeTeamScore;
//
//    @Column(nullable = false)
//    private Integer awayTeamScore;
//
//    @Column
//    private String round;
//
//    @Column
//    private String winner;
//
//    @OneToMany(mappedBy = "match")
//    private List<GoalScorer> goalScorers;
//
//    // Getters and setters
//}
