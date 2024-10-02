package com.soccerapp.app.controller;

import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.service.UserService;
import jakarta.validation.Valid;
import org.hibernate.engine.jdbc.mutation.spi.Binding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create/users")
    public String createUser(Model model) {
        // Add an empty UserDto object to the model to bind form fields
        model.addAttribute("user", new UserDto());
        // Return the name of the Thymeleaf template (without the .html extension)
        return "create_user";
    }

    @PostMapping("/create/users")
    public String handleUserCreation(@ModelAttribute("user") UserDto userDto, Model model) {
        // Create a new user
        UserDto createdUser = userService.createUser(userDto);

        // Optionally, add the created user to the model
        model.addAttribute("user", createdUser);

        // Add all users to the model to view the updated list
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);

        // Redirect to the list of users after creating a new user
        return "redirect:/users";
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
                             BindingResult result){
        if (result.hasErrors()) {
            return "edit_user";
        }
        // Update the user details
        user.setId(id);
        userService.updateUserFields(user);
        return "redirect:/users";
    }

}
