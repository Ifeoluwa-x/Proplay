//package com.soccerapp.app.models;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table(name = "team")
//public class Team {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer teamId;
//
//    @Column(nullable = false)
//    private String teamName;
//
//    @Column
//    private String teamLogo;
//
//    @ManyToOne
//    @JoinColumn(name = "team_captain")
//    private Player teamCaptain;
//
//    @ManyToOne
//    @JoinColumn(name = "team_vice")
//    private Player teamVice;
//
//    @Column(nullable = false)
//    private String teamLocation;
//
//    @Column
//    private LocalDateTime createdAt;
//
//    @Column
//    private String teamDesc;
//
//    @ManyToOne
//    @JoinColumn(name = "owner_id", nullable = false)
//    private Owner owner;
//
//    @OneToMany(mappedBy = "team")
//    private List<Player> players;
//
//    // Getters and setters
//}
