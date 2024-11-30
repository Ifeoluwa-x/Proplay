package com.soccerapp.app.controller;

import com.soccerapp.app.dto.MatchDto;
import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.*;
import com.soccerapp.app.repository.MatchTeamReqRepository;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.service.*;
import com.soccerapp.app.service.impl.MatchServiceImpl;
//import com.soccerapp.app.utils.MatchTimeFormatter;
import com.soccerapp.app.utils.DateUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.soccerapp.app.models.Match.MatchMode.TEAM;
import static com.soccerapp.app.models.Match.MatchType.FRIENDLY;

@Controller
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private TeamReqService teamReqService;
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;

    @Autowired
    private UserService userService;
    private PlayerService playerService;
    private TeamService teamService;
    private MatchService matchService;
    private MatchPartService matchPartService;
    private MatchTeamService matchTeamService;
    private MatchTeamReqRepository matchTeamReqRepository;



    @Autowired
    public MatchController(UserService userService, PlayerService playerService,
                           TeamService teamService, TeamReqService teamReqService,
                           MatchService matchService, MatchPartService matchPartService,
                           MatchTeamService matchTeamService,
                           MatchTeamReqRepository matchTeamReqRepository) {
        this.userService = userService;
        this.playerService = playerService;
        this.teamService = teamService;
        this.teamReqService = teamReqService;
        this.matchService =matchService;
        this.matchPartService = matchPartService;
        this.matchTeamService = matchTeamService;
        this.matchTeamReqRepository = matchTeamReqRepository;

    }


//    @GetMapping("/create/team/friendly")
//    public String createTeamFriendly(HttpSession session, Model model) {
//        // Retrieve the logged-in user from the session
//        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
//
//        if (loggedInUser == null) {
//            // Redirect to the login page if no user is logged in
//            return "redirect:/login";
//        }
//
//        List<Match.MatchMode> allowedMatchModes = Arrays.asList(Match.MatchMode.TEAM);
//
//        // Pass only FRIENDLY type for MatchType as an example
//        List<Match.MatchType> allowedMatchTypes = Arrays.asList(Match.MatchType.FRIENDLY);
//
//        // Add loggedInUser and player attributes to the model for the form
//        model.addAttribute("user", loggedInUser);
//        model.addAttribute("MatchMode", allowedMatchModes);
//        model.addAttribute("MatchType", allowedMatchTypes);
//        model.addAttribute("match", new MatchDto()); // Initialize with PlayerDto to match form binding
//        return "player_friendly_form";
//    }


    @PostMapping("/create/player/friendly")
    public String createPlayerFriendly(
            @Valid @ModelAttribute("match") MatchDto matchDto,
            BindingResult bindingResult,
            Model model, HttpSession session) {

        // Set user ID from session if needed
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        if (loggedInUser != null && matchDto.getUser() == null) {
            matchDto.setUser(loggedInUser);
            System.out.println("User set in PlayerDto: " + matchDto.getUser().getId());
        }

        // Debugging: Log the playerDto before saving
        System.out.println("Before saving, MatchDto: " + matchDto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("loggedInUser", loggedInUser);
            return "player_friendly_form"; // Stay on form if there are validation errors
        }

        // Create and save the player profile
        MatchDto createdMatch = matchService.createMatch(matchDto);
        model.addAttribute("match", createdMatch);

        // Redirect back to the GET method to show the form again
        return "redirect:/match/join/friendly/list";
    }

    @GetMapping("/join/friendly/list")
    public String listMatches(HttpSession session,
                              @RequestParam(value = "location", required = false) String location,
                              Model model) {

        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            List<Player> players = playerService.getPlayersByUserId(userId);

            if (!players.isEmpty()) {
                Player player = players.get(0);
                model.addAttribute("player", player);

                List<Match.MatchMode> allowedMatchModes = Arrays.asList(Match.MatchMode.PLAYER);

                // Pass only FRIENDLY type for MatchType as an example
                List<Match.MatchType> allowedMatchTypes = Arrays.asList(FRIENDLY);

                // Add loggedInUser and player attributes to the model for the form
                model.addAttribute("user", loggedInUser);
                model.addAttribute("MatchMode", allowedMatchModes);
                model.addAttribute("MatchType", allowedMatchTypes);
                model.addAttribute("match", new MatchDto());

                // Fetch all matches from the service
                List<MatchDto> matches = matchService.findAllMatches();

                List<MatchDto> joinedMatches = matches.stream()
                        .filter(match -> matchPartService.hasPlayerJoinedMatch(match.getMatchId(), player.getId()))
                        .collect(Collectors.toList());

                // Format dates and calculate available spots for joined matches.
                matches.forEach(match -> {
                    String formattedDate = DateUtils.formatLocalDateTime(match.getMatchDate());
                    match.setFormattedDate(formattedDate);

                    int participantCount = matchPartService.countParticipantsByMatchId(match.getMatchId());
                    int availableSpots = match.getMaxPlayers() - participantCount;
                    match.setAvailableSpots(availableSpots);
                    match.setJoinDisabled(availableSpots <= 0);

                    // Check if the player has joined this match
                    boolean hasJoined = matchPartService.hasPlayerJoinedMatch(match.getMatchId(), player.getId());
                    match.setHasPlayerJoined(hasJoined); // Ensure MatchDto has this field
                });



                // Filter matches player has not joined
                matches = matches.stream()
                        .filter(match -> !matchPartService.hasPlayerJoinedMatch(match.getMatchId(), player.getId()))
                        .collect(Collectors.toList());

                // If a location filter is provided, filter matches by location
                if (location != null && !location.isEmpty()) {
                    matches = matches.stream()
                            .filter(match -> match.getLocation().toLowerCase().contains(location.toLowerCase()))
                            .collect(Collectors.toList());
                }

                // Filter matches where the matchMode is TEAM
                matches = matches.stream()
                        .filter(match -> match.getMatchMode() == Match.MatchMode.PLAYER)  // Only include matches with matchMode = TEAM
                        .collect(Collectors.toList());

                // Format dates and calculate available spots
                matches.forEach(match -> {
                    String formattedDate = DateUtils.formatLocalDateTime(match.getMatchDate());
                    match.setFormattedDate(formattedDate);

                    int participantCount = matchPartService.countParticipantsByMatchId(match.getMatchId());
                    int availableSpots = match.getMaxPlayers() - participantCount;
                    match.setAvailableSpots(availableSpots);
                    match.setJoinDisabled(availableSpots <= 0);

                    // Check if the player has joined this match
                    boolean hasJoined = matchPartService.hasPlayerJoinedMatch(match.getMatchId(), player.getId());
                    match.setHasPlayerJoined(hasJoined); // Ensure MatchDto has this field
                });
                model.addAttribute("players", players);
                model.addAttribute("matches", matches);
                model.addAttribute("joinedMatches", joinedMatches);
                model.addAttribute("location", location);
            } else {
                // Add loggedInUser and player attributes to the model for the form
                model.addAttribute("user", loggedInUser);
                model.addAttribute("player", null);

                return "player_friendly_form";
            }
        } else {
            return "redirect:/login";
        }

        return "player_friendly_form"; // Return the Thymeleaf template
    }



    // Join a friendly match
    @PostMapping("/join/{matchId}/{playerId}")
    public String joinFriendlyMatch(@PathVariable Long matchId, @PathVariable Long playerId, RedirectAttributes redirectAttributes) {

        // Check if the player has already joined the match
        boolean hasJoined = matchPartService.hasPlayerJoinedMatch(matchId, playerId);

        if (hasJoined) {
            // Add the error message to RedirectAttributes
            redirectAttributes.addFlashAttribute("errorMessage", "You have already joined this match.");
            return "redirect:/match/join/friendly/list"; // Redirect back to the match list
        }

        // Add the player to the match
        matchPartService.joinFriendly(matchId, playerId);

        // Redirect back to the match list after successfully joining
        redirectAttributes.addFlashAttribute("successMessage", "You have successfully joined the match.");
        return "redirect:/match/join/friendly/list";
    }


    @GetMapping("/team/list/{teamId}")
    public String listTeamForFriendlies(HttpSession session,@PathVariable Long teamId,
                                        @RequestParam(value = "location", required = false) String location,
                                        Model model) {

        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        // Fetch all teams or filter by location if provided
        List<TeamDto> teams = location == null || location.isEmpty()
                ? teamService.getAllTeams() // Retrieve all teams
                : teamService.getTeamsByLocation(location); // Retrieve teams by location

        // Filter out the team with the given teamId
        List<TeamDto> filteredTeams = teams.stream()
                .filter(team -> !team.getTeamId().equals(teamId)) // Exclude the team with the passed teamId
                .collect(Collectors.toList());


        model.addAttribute("user", loggedInUser);
        model.addAttribute("initTeamId", teamId);
        model.addAttribute("teams", filteredTeams); // Add teams to the model
        model.addAttribute("location", location); // Add location for pre-filling the search input
        return "viewTeam";
    }


    @GetMapping("/team/friendly/view/{userId}/{initTeamId}/{teamId}")
    public String viewTeamProfileForFriendlies(HttpSession session,@PathVariable Long teamId,
                                        @PathVariable Long initTeamId,Model model) {

        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        //Get initial Team
        Team initialTeam = teamService.getTeamById(initTeamId);

        // Fetch the specific team details by teamId
        Team team = teamService.getTeamById(teamId);



        model.addAttribute("user", loggedInUser);
        model.addAttribute("initTeamId", initTeamId);
        model.addAttribute("team", team);
        model.addAttribute("teamId", teamId);
        model.addAttribute("match", new MatchDto());
        return "teamFriendlyProfile";
    }

    @PostMapping("/create/team/friendly/{initTeamId}/{teamId}")
    public String createTeamFriendly(
            @Valid @ModelAttribute("match") MatchDto matchDto,
            @PathVariable Long initTeamId,
            @PathVariable Long teamId,
            BindingResult bindingResult,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Set user ID from session if needed
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        long userId = loggedInUser.getId();
        if (loggedInUser != null) {
            matchDto.setUser(loggedInUser);
            System.out.println("User set in MatchDto: " + matchDto.getUser().getId());
        }

        // Debugging: Log the matchDto before saving
        System.out.println("Before saving, MatchDto: " + matchDto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("loggedInUser", loggedInUser);
            return "teamFriendlyProfile"; // Stay on form if there are validation errors
        }

        // Check if a pending request already exists
        List<MatchTeamReq> existingRequests = matchTeamReqRepository.findByHomeTeam_teamIdAndAwayTeam_teamIdAndDecision(
                initTeamId, teamId, "Pending");

        if (!existingRequests.isEmpty()) {
            // If a pending request exists, add an error message to the redirect
            redirectAttributes.addFlashAttribute("errorMessage", "A pending match request already exists between these teams.");
            return "redirect:/match/team/friendly/view/" + userId + "/" + initTeamId + "/" + teamId;
        }

        // Set additional properties instead of overwriting matchDto
        matchDto.setMatchMode(Match.MatchMode.TEAM);
        matchDto.setType(Match.MatchType.FRIENDLY);

        // If matchMode is TEAM, set the location (or other fields) to null
        if (matchDto.getMatchMode() == Match.MatchMode.TEAM) {
            matchDto.setMaxPlayers(24);  // This will set the location to NULL
        }

        // Create and save the match
        MatchDto createdMatch = matchService.createTeamMatch(matchDto);
        Long matchId = createdMatch.getMatchId();
        matchTeamService.sendRequest(matchId, initTeamId, teamId);

        model.addAttribute("match", createdMatch);

        // Add success message to flash attributes
        redirectAttributes.addFlashAttribute("successMessage", "Team friendly match request has been successfully created!");

        // Redirect back to the GET method to show the form again
        return "redirect:/match/team/friendly/view/" + userId + "/" + initTeamId + "/" + teamId;
    }


}
