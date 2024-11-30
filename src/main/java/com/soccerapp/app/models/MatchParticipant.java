package com.soccerapp.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "match_participant")
public class MatchParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long participantId;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = true)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;



    // Getters and setters omitted for brevity
}

