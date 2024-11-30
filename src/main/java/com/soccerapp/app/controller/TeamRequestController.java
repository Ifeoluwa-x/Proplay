package com.soccerapp.app.controller;

import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.models.TeamRequest;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.service.PlayerService;
import com.soccerapp.app.service.TeamReqService;
import com.soccerapp.app.service.TeamService;
import com.soccerapp.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TeamRequestController {


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


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public TeamRequestController(UserService userService, PlayerService playerService, TeamService teamService, TeamReqService teamReqService  ) {
        this.userService = userService;
        this.playerService = playerService;
        this.teamService = teamService;
        this.teamReqService = teamReqService;
    }


    @GetMapping("/requests")
    public String viewRequests(HttpSession session, Model model) {
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
        List<TeamRequest> requests = teamReqService.getRequestsForPlayer(playerId);
        model.addAttribute("user",loggedInUser );
        model.addAttribute("requests", requests);
        return "team_requests";  // HTML template name
    }



    // Send a request to a player
    @PostMapping("/team/{teamId}/sendRequest/{playerId}")
    public String sendTeamRequest(@PathVariable Long teamId, @PathVariable Long playerId) {
        // Call the service method to send the team request with predefined custom message
        teamReqService.sendRequest(teamId, playerId);

        // Redirect to the profile page or another relevant page after sending the request
        return "redirect:/team/{teamId}/players";  // Adjust the redirect URL as needed
    }




    @PostMapping("/requests/accept/{id}/addPlayer/{teamId}/{playerId}")
    public String acceptRequest(
            @PathVariable Long id,
            @PathVariable Long teamId,
            @PathVariable Long playerId,
            RedirectAttributes redirectAttributes) {

        System.out.println("Request method: POST, URL: /requests/accept/" + id);  // Debugging

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        teamReqService.acceptRequest(id);
        player.setTeam(team);
        playerRepository.save(player);

        // Add a success message to RedirectAttributes
        redirectAttributes.addFlashAttribute("message", "Request accepted successfully, and you've been added to the team.");

        return "redirect:/requests";  // Redirect back to the requests page
    }



    // Decline team request
    @PostMapping("/decline/{id}")
    public String declineRequest(@PathVariable Long id) {
        teamReqService.declineRequest(id);
        return "redirect:/requests";  // Redirect back to the requests page
    }

    private Long getCurrentPlayerId() {
        // Implement logic to get the current player ID based on the logged-in user
        return 1L;  // Replace with actual logic
    }





//#########################################



    // Get unread requests for a player
    @GetMapping("/player/{playerId}/notifications")
    public String getNotifications(@PathVariable Long playerId, Model model) {
        model.addAttribute("requests", teamReqService.getUnreadRequests(playerId));
        return "notifications";  // Display unread notifications
    }

    // Mark a request as read
    @PostMapping("/teamRequest/{requestId}/markAsRead")
    public String markAsRead(@PathVariable Long requestId) {
        teamReqService.markAsRead(requestId);
        return "redirect:/profile";  // Redirect to the profile page
    }

    // Accept or decline a request
    @PostMapping("/teamRequest/{requestId}/decision")
    public String makeDecision(@PathVariable Long requestId, @RequestParam String decision) {
        teamReqService.makeDecision(requestId, decision);
        return "redirect:/profile";  // Redirect after making a decision
    }
}
