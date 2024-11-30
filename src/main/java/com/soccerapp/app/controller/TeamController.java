package com.soccerapp.app.controller;

import com.soccerapp.app.dto.MatchDto;
import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Match;
import com.soccerapp.app.models.MatchTeamReq;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.service.*;
import com.soccerapp.app.utils.DateUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
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
    MatchTeamService matchTeamService;
    UserSubscriptionService userSubscriptionService;

    public TeamController(TeamService teamService,
                          PlayerService playerService,
                          TeamReqService teamReqService,
                          MatchTeamService matchTeamService,
                          UserSubscriptionService userSubscriptionService) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.teamReqService = teamReqService;
        this.matchTeamService = matchTeamService;
        this.userSubscriptionService = userSubscriptionService;
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
        return "redirect:/team/management";
    }

    @GetMapping("/management")
    public String teamManagement(Model model, HttpSession session) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        Long loggedInUserId = loggedInUser.getId();


        boolean hasSubscription = userSubscriptionService.hasActiveSubscription(loggedInUserId);
        List<Team> teams = teamService.getTeamByUserId(loggedInUserId);


        // Add loggedInUser and team attributes to the model for the form
        model.addAttribute("user", loggedInUser);
        model.addAttribute("teams", teams);
        model.addAttribute("hasSubscription", hasSubscription);

        return "team_management"; // Make sure this matches your Thymeleaf template name
    }

    @GetMapping("/view/{userId}/{teamId}")
    public String viewOwnTeamProfile(@PathVariable Long userId, @PathVariable Long teamId, Model model, HttpSession session) {
        // Optionally, you can validate if the logged-in user matches the userId
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        // Fetch the team associated with the logged-in user and the specified teamId
        List<Team> teams = teamService.getTeamByUserId(userId);

        // Fetch the specific team details by teamId
        Team team = teamService.getTeamById(teamId);

        // Add the user and team attributes to the model for the view
        model.addAttribute("user", loggedInUser);
        model.addAttribute("team", team);

        // Return the team profile view
        return "team_profile"; // Make sure this matches your Thymeleaf template name
    }


    @GetMapping("/view/{teamId}")
    public String viewTeamProfile(@PathVariable Long teamId, Model model, HttpSession session) {
        // Optionally, you can validate if the logged-in user matches the userId
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        // Fetch the specific team details by teamId
        Team team = teamService.getTeamById(teamId);

        // Add the user and team attributes to the model for the view
        model.addAttribute("user", loggedInUser);
        model.addAttribute("team", team);

        // Return the team profile view
        return "teamProfile"; // Make sure this matches your Thymeleaf template name
    }















    @GetMapping("/{teamId}/players")
    public String listPlayers(  HttpSession session,@PathVariable Long teamId, Model model) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
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
        model.addAttribute("user", loggedInUser);
        model.addAttribute("team", team);
        model.addAttribute("players", players);

        return "players";
    }


    // List all MatchTeamReq where teamId is in the awayTeam column and decision is "Pending"
    @GetMapping("/match/Requests/{teamId}")
    public String listPendingAwayTeamMatches(HttpSession session, @PathVariable Long teamId, Model model) {

        // Optionally, you can validate if the logged-in user matches the userId
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        // Retrieve all match team requests where the team is in the awayTeam column and decision is "Pending"
        List<MatchTeamReq> incomingRequests = matchTeamService.findPendingAwayTeamMatches(teamId);


        // Retrieve all match team requests where the team is in the homeTeam column and decision is "Pending" or "Declined"
        List<MatchTeamReq> homeTeamRequests = matchTeamService.findHomeTeamMatchesByStatus(teamId, "Pending", "Declined");

        // Retrieve match requests where the decision is "Accepted" and teamId is in homeTeam or awayTeam
        List<MatchTeamReq> acceptedRequests = matchTeamService.findAcceptedMatchRequests(teamId);


        // Format dates and calculate available spots for joined matches.
        homeTeamRequests.forEach(matchreq -> {
            String formattedDate = DateUtils.formatLocalDateTime(matchreq.getMatch().getMatchDate());
            matchreq.setFormattedDate(formattedDate);
        });

        // Format dates and calculate available spots for joined matches.
        acceptedRequests.forEach(match -> {
            String formattedDate = DateUtils.formatLocalDateTime(match.getMatch().getMatchDate());
            match.setFormattedDate(formattedDate);
        });






        // Add the teamId to the model
        model.addAttribute("teamId", teamId);

        // Add the list of pending match team requests to the model
        model.addAttribute("user", loggedInUser);
        model.addAttribute("incomingRequests", incomingRequests);
        model.addAttribute("homeTeamRequests", homeTeamRequests);
        model.addAttribute("acceptedRequests", acceptedRequests);


        // Return the view to display the match requests
        return "matchTeamRequests"; // Replace with your actual view name
    }

    @PostMapping("/match/request/accept/{teamId}/{matchId}/{matchReqId}")
    public String acceptRequest(@PathVariable Long teamId,@PathVariable Long matchId,
                                @PathVariable Long matchReqId,
                                RedirectAttributes redirectAttributes) {
        System.out.println("Request method: POST, URL: /requests/accept/"+ matchId + matchReqId);  // Debugging
        matchTeamService.acceptTeamMatchRequest(matchReqId);

        // Add success message to the redirect attributes
        redirectAttributes.addFlashAttribute("successMessage", "Match request accepted successfully!");
        return "redirect:/team/match/Requests/" + teamId;  // Redirect back to the requests page
    }


//    // List all MatchTeamReq where teamId is in the homeTeam column and decision is "Pending" or "Declined"
//    @GetMapping("/match/Requests/{teamId}")
//    public String listHomeTeamPendingAndDeclinedMatches(HttpSession session, @PathVariable Long teamId, Model model) {
//
//        // Optionally, you can validate if the logged-in user matches the userId
//        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
//
//        // Retrieve all match team requests where the team is in the homeTeam column and decision is "Pending" or "Declined"
//        List<MatchTeamReq> homeTeamRequests = matchTeamService.findHomeTeamMatchesByStatus(teamId, "Pending", "Declined");
//
//        // Add the teamId to the model
//        model.addAttribute("teamId", teamId);
//
//        // Add the list of match team requests to the model
//        model.addAttribute("user", loggedInUser);
//        model.addAttribute("homeTeamRequests", homeTeamRequests);
//
//        // Return the view to display the match requests
//        return "matchTeamRequests"; // Replace with your actual view name
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

