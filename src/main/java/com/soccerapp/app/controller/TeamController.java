package com.soccerapp.app.controller;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.service.PlayerService;
import com.soccerapp.app.service.TeamReqService;
import com.soccerapp.app.service.TeamService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    PlayerService playerService;
    TeamReqService teamReqService;


    public TeamController(TeamService teamService, PlayerService playerService, TeamReqService teamReqService) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.teamReqService = teamReqService;
    }

    @GetMapping("/create")
    public String createTeam(Model model, HttpSession session) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        // Add loggedInUser and player attributes to the model for the form
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("team", new TeamDto()); // Initialize with TeamDto to match form binding
        return "team_form"; // Make sure this matches your Thymeleaf template name
    }

    @PostMapping("/create")
    public String createTeam(
            @Valid @ModelAttribute("team") TeamDto teamDto,
            BindingResult bindingResult,
            Model model, HttpSession session) {

        // Get the logged-in user from session
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        // Set the owner ID from the logged-in user if it's not already set in the TeamDto
        if (loggedInUser != null && teamDto.getOwnerId() == null) {
            teamDto.setOwnerId(loggedInUser.getId());
        }

        // Debugging: Log the teamDto before saving
        System.out.println("Before saving, TeamDto: " + teamDto);

        // Check if the teamDto has errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("loggedInUser", loggedInUser);
            return "team_form"; // Stay on the form if there are validation errors
        }

        // Map TeamDto to Team entity
        TeamDto createdTeam = teamService.createTeam(teamDto);

        // Debugging: Log the saved TeamDto
        System.out.println("After saving, TeamDto: " + createdTeam);

        // Add createdTeam to the model
        model.addAttribute("team", createdTeam);

        // Redirect back to the profile page
        return "redirect:/profile";
    }










    @GetMapping("/{teamId}/players")
    public String listPlayers(@PathVariable Long teamId, Model model) {
        List<PlayerDto> players = playerService.findPlayersNotInTeam(teamId);

        // Retrieve the team by ID
        Team team = teamService.getTeamById(teamId);

        // Check for each player if the request has been sent
        for (PlayerDto player : players) {
            boolean isRequestSent = teamReqService.isRequestSent(teamId, player.getPlayerId());
            player.setRequestSent(isRequestSent);  // Add a field in PlayerDto to store the request status
        }

        if (team == null) {
            // Handle the case where the team is not found (optional)
            throw new IllegalArgumentException("Team not found for ID: " + teamId);
        }

        // Add team and players to the model
        model.addAttribute("team", team);
        model.addAttribute("players", players);

        return "players";
    }

//    @GetMapping("/addPlayer/{teamId}/{playerId}")
//    public String addPlayerToTeam(@PathVariable Long teamId, @PathVariable Long playerId) {
//        try {
//            playerService.addPlayerToTeam(playerId, teamId);  // Call the service method to handle the logic
//            return "redirect:/profile";  // Redirect back to the profile page (or wherever you want)
//        } catch (RuntimeException e) {
//            // Handle the error (you could return an error page or show a message)
//            return "error";
//        }
//    }




























//    // List all teams
//    @GetMapping("/list")
//    public String viewTeams(HttpSession session, Model model) {
//        // Retrieve the logged-in user from the session
//        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
//
//        // Get the player ID from the logged-in user (assuming it's stored in 'id' or 'playerId' field)
//        Long playerId = loggedInUser.getId(); // Or use loggedInUser.getPlayerId() if that's the field name
//
//        // Fetch all teams
//        List<Team> teams = teamService.getAllTeams();
//
//        // Map each team to a boolean indicating if the request has been sent
//        Map<Long, Boolean> requestSentMap = new HashMap<>();
//        for (Team team : teams) {
//            requestSentMap.put(team.getTeamId(), playerReqService.isRequestSent(playerId, team.getTeamId()));
//        }
//
//        // Add teams, playerId, and requestSentMap to the model
//        model.addAttribute("teams", teams);
//        model.addAttribute("playerId", playerId);
//        model.addAttribute("requestSentMap", requestSentMap); // Add the map to the model
//
//        // Return the name of the Thymeleaf template
//        return "team_list";
//    }




    // View all requests for a specific team
//    @GetMapping("/{teamId}/requests")
//    public String viewRequests(@PathVariable Long teamId, HttpSession session, Model model) {
//
//        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
//        // Retrieve all player requests for the specific team
//        List<PlayerRequest> requests = playerRequestRepository.findByTeam_TeamId(teamId);
//        model.addAttribute("requests", requests);
//        return "player_request";  // Thymeleaf template for displaying requests
//    }
//
//    // Accept a request
//    @PostMapping("/{teamId}/acceptRequest/{requestId}")
//    public String acceptRequest(@PathVariable Long teamId, @PathVariable Long requestId) {
//        teamService.acceptPlayerRequest(requestId);
//        return "redirect:/team/{teamId}/requests";  // Redirect to the team requests page
//    }
//
//    // Decline a request
//    @PostMapping("/{teamId}/declineRequest/{requestId}")
//    public String declineRequest(@PathVariable Long teamId, @PathVariable Long requestId) {
//        teamService.declinePlayerRequest(requestId);
//        return "redirect:/team/{teamId}/requests";  // Redirect to the team requests page
//    }
}

