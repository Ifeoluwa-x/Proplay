package com.soccerapp.app.controller;


import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.Player;
import com.soccerapp.app.models.User;
//import com.soccerapp.app.security.CustomUserDetail;
import com.soccerapp.app.service.PlayerService;
import com.soccerapp.app.service.UserService;
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

import java.util.List;

@Controller
public class AuthController {
    private UserService userService;
    private PlayerService playerService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserService userService, PlayerService playerService) {
        this.userService = userService;
        this.playerService = playerService;
    }

    //Displays Index
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new UserDto());
        return "home";
    }

    @GetMapping("/register")
    public String register(Model model) {
        // Add an empty UserDto object to the model to bind form fields
        model.addAttribute("user", new UserDto());
        // Return the name of the Thymeleaf template (without the .html extension)
        return "register";
    }

    @PostMapping("/register")
    public String handleUserCreation(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult bindingResult,
            Model model) {

        UserDto existingUserEmail = userService.findUserByEmail(userDto.getEmail());
        if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }

        if (bindingResult.hasErrors()) {
            return "register"; // Make sure this matches your HTML template name
        }

        UserDto createdUser = userService.createUser(userDto);
        model.addAttribute("user", createdUser);

        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);

        return "redirect:/login?success";
    }

    // Login Page
    @GetMapping("/login")
    public String login(@RequestParam(value = "success", required = false) String success, Model model) {
        model.addAttribute("user", new UserDto()); // Ensure this line is present
        if (success != null) {
            model.addAttribute("message", "Account created successfully. Please login below.");
        }
        return "login"; // Return the name of your login template
    }

}
