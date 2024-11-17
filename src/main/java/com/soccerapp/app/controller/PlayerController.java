package com.soccerapp.app.controller;


import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.service.PlayerReqService;
import com.soccerapp.app.service.PlayerService;
import com.soccerapp.app.service.TeamService;
import com.soccerapp.app.service.impl.PlayerReqServiceImpl;
import com.soccerapp.app.service.impl.TeamServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    PlayerService playerService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerReqService playerReqService;


    @GetMapping("/profile/create")
    public String createPlayer(Model model, HttpSession session) {
    UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

    // Add loggedInUser and player attributes to the model for the form
    model.addAttribute("loggedInUser", loggedInUser);
    model.addAttribute("player", new PlayerDto()); // Initialize with PlayerDto to match form binding
    return "player-form"; // Make sure this matches your Thymeleaf template name
    }

    @PostMapping("/profile/create")
    public String createPlayerProfile(
            @Valid @ModelAttribute("player") PlayerDto playerDto,
            BindingResult bindingResult,
            Model model, HttpSession session) {

        // Set user ID from session if needed
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        if (loggedInUser != null && playerDto.getUser() == null) {
            playerDto.setUser(loggedInUser);
            System.out.println("User set in PlayerDto: " + playerDto.getUser().getId());
        }

        // Debugging: Log the playerDto before saving
        System.out.println("Before saving, PlayerDto: " + playerDto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("loggedInUser", loggedInUser);
            return "player-form"; // Stay on form if there are validation errors
        }

        // Create and save the player profile
        PlayerDto createdPlayerProfile = playerService.createPlayerProfile(playerDto);

        // Debugging: Log the saved PlayerDto to confirm IDs
        System.out.println("After saving, PlayerDto: " + createdPlayerProfile);

        model.addAttribute("player", createdPlayerProfile);

        // Redirect back to the GET method to show the form again
        return "redirect:/profile";
    }

    @GetMapping("/view/teams")
    public String listAllTeams(HttpSession session, Model model) {
        // Retrieve the logged-in user from the session
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // Redirect to the login page if no user is logged in
            return "redirect:/login";
        }

        // Retrieve the logged-in user's ID
        Long loggedInUserId = loggedInUser.getId();

        // Fetch the player associated with this user ID
        Player player = playerService.getPlayersByUserId(loggedInUserId).stream().findFirst()
                .orElse(null);

        if (player == null) {
            model.addAttribute("message", "No player profile found. Please create a player profile.");
            return "error_page";  // Return an error view or similar
        }
        Long playerId = player.getId();

        // Retrieve all teams as TeamDto
        List<TeamDto> teams = teamService.getAllTeams();

        // Prepare requestSentMap to check if the request has already been sent for each team
        Map<Long, Boolean> requestSentMap = new HashMap<>();
        for (TeamDto team : teams) {
            boolean isRequestSent = playerReqService.isRequestSent(team.getTeamId(), playerId);
            requestSentMap.put(team.getTeamId(), isRequestSent);
        }
        if (teams == null || teams.isEmpty()) {
            model.addAttribute("message", "No teams available at the moment.");
        } else {// Add teams, requestSentMap, and the player to the model
            model.addAttribute("teams", teams);
            model.addAttribute("requestSentMap", requestSentMap);
            model.addAttribute("playerId", playerId);model.addAttribute("teams", teams);
        }

        return "team_list";  // View name
    }




}
