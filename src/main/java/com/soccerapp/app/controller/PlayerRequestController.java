package com.soccerapp.app.controller;

import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.PlayerRequest;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.models.TeamRequest;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PlayerRequestController {


    @Autowired
    private TeamReqService teamReqService;
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserService userService;
    private PlayerService playerService;
    private TeamService teamService;
    private PlayerReqService playerReqService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public PlayerRequestController(UserService userService, PlayerService playerService, TeamService teamService, PlayerReqService playerReqService, TeamReqService teamReqService  ) {
        this.userService = userService;
        this.playerService = playerService;
        this.teamService = teamService;
        this.teamReqService = teamReqService;
        this.playerReqService = playerReqService;
    }


    @GetMapping("/player/requests/{teamId}")
    public String viewPlayerRequests(@PathVariable Long teamId ,HttpSession session, Model model) {
        // Retrieve the logged-in user from the session
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // Redirect to the login page if no user is logged in
            return "redirect:/login";
        }

        // Retrieve the logged-in user's ID
        Long loggedInUserId = loggedInUser.getId();

        // Fetch the player associated with this user ID
        Team team = teamService.getTeamById(teamId);

        if (team == null) {
            model.addAttribute("message", "No Team profile found. Please create a team profile.");
            return "error_page";  // Return an error view or similar
        }
//        Long teamId = team.getTeamId();
        List<PlayerRequest> requests = playerReqService.getRequestsForTeam(teamId);
        model.addAttribute("requests", requests);
        return "player_requests";  // HTML template name
    }



    // Send a request to a player
    @PostMapping("/player/sendRequest/{teamId}/{playerId}")
    public String sendTeamRequest(@PathVariable Long teamId, @PathVariable Long playerId) {
        // Call the service method to send the team request with predefined custom message
        playerReqService.sendRequest(teamId, playerId);

        // Redirect to the profile page or another relevant page after sending the request
        return "redirect:/player/view/teams";  // Adjust the redirect URL as needed
    }




    @PostMapping("/player/requests/accept/{id}/addPlayer/{teamId}/{playerId}")
    public String acceptPlayerRequest(@PathVariable Long id,@PathVariable Long teamId, @PathVariable Long playerId) {
        System.out.println("Request method: POST, URL: /requests/accept/" + id);  // Debugging
        playerReqService.acceptRequest(id);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        player.setTeam(team);
        playerRepository.save(player);
        return "redirect:/requests";  // Redirect back to the requests page
    }


    // Decline team request
    @PostMapping("player/decline/{id}")
    public String declinePlayerRequest(@PathVariable Long id) {
        playerReqService.declineRequest(id);
        return "redirect:/requests";  // Redirect back to the requests page
    }

    private Long getCurrentPlayerId() {
        // Implement logic to get the current player ID based on the logged-in user
        return 1L;  // Replace with actual logic
    }





//#########################################



//    // Get unread requests for a player
//    @GetMapping("/player/{playerId}/notifications")
//    public String getNotifications(@PathVariable Long playerId, Model model) {
//        model.addAttribute("requests", teamReqService.getUnreadRequests(playerId));
//        return "notifications";  // Display unread notifications
//    }
//
//    // Mark a request as read
//    @PostMapping("/teamRequest/{requestId}/markAsRead")
//    public String markAsRead(@PathVariable Long requestId) {
//        teamReqService.markAsRead(requestId);
//        return "redirect:/profile";  // Redirect to the profile page
//    }
//
//    // Accept or decline a request
//    @PostMapping("/teamRequest/{requestId}/decision")
//    public String makeDecision(@PathVariable Long requestId, @RequestParam String decision) {
//        teamReqService.makeDecision(requestId, decision);
//        return "redirect:/profile";  // Redirect after making a decision
//    }
}

