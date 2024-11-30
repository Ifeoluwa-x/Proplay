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

import java.util.Collections;
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

    model.addAttribute("user", loggedInUser);
    model.addAttribute("player", new PlayerDto()); // Initialize with PlayerDto to match form binding
    return "player-form"; // Make sure this matches your Thymeleaf template name
    }

    @PostMapping("/profile/create")
    public String createPlayerProfile(
            @Valid @ModelAttribute("player") PlayerDto playerDto,
            BindingResult bindingResult,
            Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {



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
        // Add a success message as a flash attribute
        redirectAttributes.addFlashAttribute("successMessage", "Player profile successfully created");
        // Redirect back to the GET method to show the form again
        return "redirect:/profile";
    }

    @GetMapping("/view/teams")
    public String listAllTeams(HttpSession session, @RequestParam(value = "location", required = false) String location, Model model) {
        // Retrieve the logged-in user from the session
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // Redirect to the login page if no user is logged in
            return "redirect:/login";
        }

        // Retrieve the logged-in user's ID
        Long loggedInUserId = loggedInUser.getId();

        // Fetch the player associated with this user ID
        Player player = playerService.getPlayersByUserId(loggedInUserId).stream().findFirst().orElse(null);

        if (player == null) {
            model.addAttribute("user", loggedInUser);
            model.addAttribute("player", null);
            model.addAttribute("teams", Collections.emptyList());  // Make sure teams is initialized
            return "team_list";
        }

        Long playerId = player.getId();

        // Retrieve all teams based on the location filter (if provided)
        List<TeamDto> teams = teamService.getAllTeams();
        if (location != null && !location.trim().isEmpty()) {
            teams = teamService.getTeamsByLocation(location);  // Filter by location
        }

        // Prepare requestSentMap to check if the request has already been sent for each team
        Map<Long, Boolean> requestSentMap = new HashMap<>();
        for (TeamDto team : teams) {
            boolean isRequestSent = playerReqService.isRequestSent(team.getTeamId(), playerId);
            requestSentMap.put(team.getTeamId(), isRequestSent);
        }

        // Add teams, requestSentMap, and the player to the model
        model.addAttribute("user", loggedInUser);
        model.addAttribute("teams", teams != null ? teams : Collections.emptyList());  // Ensure it's not null
        model.addAttribute("requestSentMap", requestSentMap);
        model.addAttribute("playerId", playerId);
        model.addAttribute("player", player);

        return "team_list";  // View name
    }






}
