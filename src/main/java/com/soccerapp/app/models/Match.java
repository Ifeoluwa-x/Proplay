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
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long matchId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_mode", nullable = false)
    private MatchMode matchMode;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MatchType type;


    @Column(name = "max_players", nullable = false)
    private Integer maxPlayers;

    // Getter and setter methods omitted for brevity

    public enum MatchMode {
        EMPTY, TEAM, PLAYER;

        @Override
        public String toString() {
            if (this == EMPTY) return ""; // Return an empty string for the default option
            return name();
        }
    }

    public enum MatchType {
        EMPTY, FRIENDLY, TOURNAMENT;

        @Override
        public String toString() {
            if (this == EMPTY) return ""; // Return an empty string for the default option
            return name();
        }
    }

}
