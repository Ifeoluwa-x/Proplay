package com.soccerapp.app.service.impl;

import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.exception.ResourceNotFoundException;
import com.soccerapp.app.models.Role;
import com.soccerapp.app.models.User;
import com.soccerapp.app.repository.RoleRepository;
import com.soccerapp.app.repository.UserRepository;
import com.soccerapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void updatePasswords() {
        // Assume this method retrieves all users
        List<com.soccerapp.app.models.User> users = userRepository.findAll();

        for (com.soccerapp.app.models.User user : users) {
            // For this example, we're assuming you have a way to get the plain text password.
            // In reality, you would need to gather these securely.
            String plainTextPassword = user.getPassword(); // Modify this as needed

            // Encode the password
            String encodedPassword = passwordEncoder.encode(plainTextPassword);
            user.setPassword(encodedPassword); // Set the encoded password

            // Save the user back to the database
            userRepository.save(user);
            userRepository.flush(); // Forces Hibernate to execute the insert immediately
        }
    }

    public UserDto createUser(UserDto userDto) {
        // Convert UserDto to User entity
        User user = mapToUser(userDto);
        Role role = roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(role));
        // Save the user in the database
        String plainTextPassword = user.getPassword(); // Modify this as needed

        // Encode the password
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        user.setPassword(encodedPassword); // Set the encoded password
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

//    public User findUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
    public UserDto findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToUserDto) // Only map if the user is found
                .orElse(null); // Return null if no user is found
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
        userRepository.updateUserFields( userDto.getId(), userDto.getEmail(), userDto.getLocation());
    }
















    private User mapToUser(UserDto user) {
        User userDto = User.builder()
                .id(user.getId())
                .fname(user.getFname())
                .lname(user.getLname())
                .email(user.getEmail())
                .password(user.getPassword())          // Added password field
                .age(user.getAge())
                .location(user.getLocation())           // Added location field
                .sex(user.getSex())                     // Added sex field
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
                .age(user.getAge())   // Added availability field
                .location(user.getLocation())           // Added location field
                .sex(user.getSex())                 // Added team field
                .createdAt(user.getCreatedAt())        // Added createdAt field
                .updatedAt(user.getUpdatedAt())        // Added updatedAt field
                .build();
        return userDto;
    }

    @Transactional
    public void toggleFname(String currentName, String newName) {
        List<User> users = userRepository.findByFname(currentName);
        for (User user : users) {
            user.setFname(newName);
        }
        userRepository.saveAll(users);
    }
}
