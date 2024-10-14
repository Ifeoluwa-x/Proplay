package com.soccerapp.app.controller;

import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // Create New Player
    @GetMapping("/create/players")
    public String createPlayer(Model model) {
        // Add an empty PlayerDto object to the model to bind form fields
        model.addAttribute("player", new PlayerDto());
        // Return the name of the Thymeleaf template (without the .html extension)
        return "create_player";
    }

    @PostMapping("/create/players")
    public String handlePlayerCreation(@ModelAttribute("player") PlayerDto playerDto, Model model) {
        // Create a new player
        PlayerDto createdPlayer = playerService.createPlayer(playerDto);

        // Optionally, add the created player to the model
        model.addAttribute("player", createdPlayer);



        // Redirect to the list of players after creating a new player
        return "redirect:/players";
    }


}
