package com.soccerapp.app.controller;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.dto.TeamDto;
import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.Team;
import com.soccerapp.app.models.TeamRequest;
import com.soccerapp.app.models.User;
//import com.soccerapp.app.security.CustomUserDetail;
import com.soccerapp.app.repository.PlayerRepository;
import com.soccerapp.app.repository.TeamRepository;
import com.soccerapp.app.service.PlayerService;
import com.soccerapp.app.service.TeamReqService;
import com.soccerapp.app.service.TeamService;
import com.soccerapp.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.soccerapp.app.utils.DateUtil.getDayWithSuffix;

@Controller
public class UserController {



    @Autowired
    private UserService userService;
    private PlayerService playerService;
    private TeamService teamService;
    private TeamReqService teamReqService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserController(UserService userService, PlayerService playerService, TeamService teamService, TeamReqService teamReqService  ) {
        this.userService = userService;
        this.playerService = playerService;
        this.teamService = teamService;
        this.teamReqService = teamReqService;
    }

    @GetMapping("/profile")
    public String getPlayersForLoggedInUser(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();

        UserDto user = userService.findUserByEmail(loggedInUserEmail);
        Long loggedInUserId = user.getId();

        List<Player> players = playerService.getPlayersByUserId(loggedInUserId);

        List<Team> teams = teamService.getTeamByUserId(loggedInUserId);

        // Format the createdAt and updatedAt dates
        // Create a DateTimeFormatter for the full month name
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM, yyyy");

        // Format createdAt and updatedAt
        String formattedCreatedAt = user.getCreatedAt() != null
                ? getDayWithSuffix(user.getCreatedAt().getDayOfMonth()) + " " + user.getCreatedAt().format(monthYearFormatter)
                : null;

        String formattedUpdatedAt = user.getUpdatedAt() != null
                ? getDayWithSuffix(user.getUpdatedAt().getDayOfMonth()) + " " + user.getUpdatedAt().format(monthYearFormatter)
                : null;

        // Add user and formatted dates to the model
        model.addAttribute("user", user);
        model.addAttribute("formattedCreatedAt", formattedCreatedAt);
        model.addAttribute("formattedUpdatedAt", formattedUpdatedAt);
        model.addAttribute("players", players);
        model.addAttribute("teams", teams);

        // Store the user in the session
        session.setAttribute("loggedInUser", user);

        return "profile";
    }

//    @GetMapping("/users/{id}")
//    public String userDetail(@PathVariable("id") Long id, Model model) {
//        UserDto user = userService.findUserById(id);
//        model.addAttribute("user", user);
//        return "users_detail";}

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/delete/user/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }


    @GetMapping("/update/user/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model,HttpSession session) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        UserDto userDto = userService.findUserById(id);
        model.addAttribute("user", userDto);
        // Return the name of the Thymeleaf template (without the .html extension)
        return "edit_user";
    }

    // Handle form submission for updating the user
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute("user") UserDto userDto,
                             RedirectAttributes redirectAttributes) {
        userService.updateUser(id, userDto);
        Player player = playerService.getPlayersByUserId(id).get(0);

        // Add a success message as a flash attribute
        redirectAttributes.addFlashAttribute("successMessage", "User details updated successfully!");

        // Redirect to the profile page
        return "redirect:/profile";
    }


    @GetMapping("/competition-hub")
    public String compHub(Model model,HttpSession session) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");
        // Return the name of the Thymeleaf template (without the .html extension)
        model.addAttribute("user", loggedInUser);
        return "comp";
    }
}
