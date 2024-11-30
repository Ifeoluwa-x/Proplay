package com.soccerapp.app.service.impl;

//import com.soccerapp.app.dto.MatchRequest;
import com.soccerapp.app.dto.MatchDto;
import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.*;
import com.soccerapp.app.repository.*;
import com.soccerapp.app.service.MatchService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public MatchServiceImpl(PlayerRepository playerRepository, UserRepository userRepository, TeamRepository teamRepository, MatchRepository matchRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }


    @Transactional
    public MatchDto createMatch(MatchDto matchDto) {
        // Convert MatchDto to Match entity
        Match match = mapToMatch(matchDto);

        // Save the Match entity
        Match savedMatch = matchRepository.save(match);

        // Convert saved Match entity back to MatchDto
        return mapToDto(savedMatch);
    }

    public MatchDto createTeamMatch(MatchDto matchDto) {
        // Convert MatchDto to Match entity
        Match match = mapToMatch(matchDto);

        // Save the Match entity
        Match savedMatch = matchRepository.save(match);

        // Convert saved Match entity back to MatchDto
        return mapToDto(savedMatch);
    }



    public List<MatchDto> findAllMatches() {
        List<Match> matches = matchRepository.findAll();
        return matches.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<MatchDto> getMatchesByLocation(String location) {
        List<Match> matches = matchRepository.findByLocation(location);

        // Manual mapping of Team to TeamDto
        List<MatchDto> matchDtos = new ArrayList<>();
        for (Match match : matches) {
            MatchDto dto = new MatchDto();
            dto.setMatchDate(match.getMatchDate());
            dto.setType(match.getType());
            dto.setLocation(match.getLocation());
            dto.setMatchMode(match.getMatchMode());
            dto.setMaxPlayers(match.getMaxPlayers());

            // Add any other fields you want to include
            matchDtos.add(dto);
        }
        return matchDtos;
    }





    private Match mapToMatch(MatchDto matchDto) {
        User user = userRepository.findById(matchDto.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return Match.builder()
                .matchId(matchDto.getMatchId())
                .matchDate(matchDto.getMatchDate())
                .user(user)
                .matchMode(matchDto.getMatchMode())
                .type(matchDto.getType())
                .maxPlayers(matchDto.getMaxPlayers())
                .location(matchDto.getLocation())
                .build();
    }

    private MatchDto mapToDto(Match match) {
        UserDto userDto = null;
        if (match.getUser() != null) {
            userDto = UserDto.builder()
                    .id(match.getUser().getId())
                    .fname(match.getUser().getFname())
                    .lname(match.getUser().getLname())
                    .email(match.getUser().getEmail())
                    .age(match.getUser().getAge())
                    .location(match.getUser().getLocation())
                    .sex(match.getUser().getSex())
                    .build();
        }


        return MatchDto.builder()
                .matchId(match.getMatchId())
                .matchDate(match.getMatchDate())
                .matchMode(match.getMatchMode())
                .type(match.getType())
                .maxPlayers(match.getMaxPlayers())
                .location(match.getLocation())
                .user(userDto)
                .build();
    }
}
