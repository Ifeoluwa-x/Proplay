package com.soccerapp.app.controller;

import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.User;
//import com.soccerapp.app.security.CustomUserDetail;
import com.soccerapp.app.service.PlayerService;
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

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.soccerapp.app.utils.DateUtil.getDayWithSuffix;

@Controller
public class UserController {
    private UserService userService;
    private PlayerService playerService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserController(UserService userService, PlayerService playerService) {
        this.userService = userService;
        this.playerService = playerService;
    }

    @GetMapping("/profile")
    public String getPlayersForLoggedInUser(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();

        UserDto user = userService.findUserByEmail(loggedInUserEmail);
        Long loggedInUserId = user.getId();

        List<Player> players = playerService.getPlayersByUserId(loggedInUserId);

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

        // Store the user in the session
        session.setAttribute("loggedInUser", user);

        return "profile";
    }

    @GetMapping("player/profile/create")
    public String createPlayer(Model model, HttpSession session) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("player", new Player());
        return "player-form"; // This should be the name of your Thymeleaf template file (without .html)
    }

    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable("id") Long id, Model model) {
        UserDto user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "users_detail";}

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
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        // Fetch the user details to edit
        UserDto userDto = userService.findUserById(id);
        model.addAttribute("user", userDto);
        // Return the name of the Thymeleaf template (without the .html extension)
        return "edit_user";
    }

    @PostMapping("/update/user/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("user") UserDto user,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            logger.error("Error updating user with ID {}: {}", id, bindingResult.getAllErrors());
            return "edit_user";
        }
        // Update the user details
        user.setId(id);
        userService.updateUserFields(user);
        return "redirect:/users";
    }
}
