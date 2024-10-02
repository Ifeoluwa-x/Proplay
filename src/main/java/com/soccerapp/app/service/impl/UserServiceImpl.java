package com.soccerapp.app.service.impl;

import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.exception.ResourceNotFoundException;
import com.soccerapp.app.models.User;
import com.soccerapp.app.repository.UserRepository;
import com.soccerapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto userDto) {
        // Convert UserDto to User entity
        User user = mapToUser(userDto);

        // Save the user in the database
        User savedUser = userRepository.save(user);

        // Convert saved User entity back to UserDto and return
        return mapToUserDto(savedUser);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserDto(user);
    }

    public void updateUser(UserDto userDto) {
        User user = mapToUser(userDto);
        userRepository.save(user);

    }

    @Transactional
    public void updateUserFields(UserDto userDto) {
        // Extract the specific fields you want to update
        userRepository.updateUserFields( userDto.getId(), userDto.getPosition(), userDto.getLocation());
    }

    private User mapToUser(UserDto user) {
        User userDto = User.builder()
                .id(user.getId())
                .fname(user.getFname())
                .lname(user.getLname())
                .email(user.getEmail())
                .password(user.getPassword())          // Added password field
                .age(user.getAge())
                .position(user.getPosition())           // Added position field
                .skillLevel(user.getSkillLevel())       // Added skillLevel field
                .availability(user.getAvailability())   // Added availability field
                .location(user.getLocation())           // Added location field
                .sex(user.getSex())                     // Added sex field
                .team(user.getTeam())                   // Added team field
                .createdAt(user.getCreatedAt())        // Added createdAt field
                .updatedAt(user.getUpdatedAt())        // Added updatedAt field
                .build();
        return userDto;
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .fname(user.getFname())
                .lname(user.getLname())
                .email(user.getEmail())
                .password(user.getPassword())          // Added password field
                .age(user.getAge())
                .position(user.getPosition())           // Added position field
                .skillLevel(user.getSkillLevel())       // Added skillLevel field
                .availability(user.getAvailability())   // Added availability field
                .location(user.getLocation())           // Added location field
                .sex(user.getSex())                     // Added sex field
                .team(user.getTeam())                   // Added team field
                .createdAt(user.getCreatedAt())        // Added createdAt field
                .updatedAt(user.getUpdatedAt())        // Added updatedAt field
                .build();
        return userDto;
    }
}
