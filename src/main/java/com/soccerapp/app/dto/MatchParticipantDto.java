package com.soccerapp.app.dto;

import com.soccerapp.app.models.Match;
import com.soccerapp.app.models.MatchParticipant;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchParticipantDto {

    private Long participantId;
    private Long playerId;
    private Long teamId;
    private Long matchId;

}
