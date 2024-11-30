package com.soccerapp.app;

import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.exception.ResourceNotFoundException;
import com.soccerapp.app.models.Role;
import com.soccerapp.app.models.User;
import com.soccerapp.app.repository.RoleRepository;
import com.soccerapp.app.repository.UserRepository;
import com.soccerapp.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        // Set up a sample UserDto before each test
        userDto = new UserDto();
        userDto.setId(54L);
        userDto.setFname("John");
        userDto.setLname("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password123");
        userDto.setAge(30);
        userDto.setSex("M");
        userDto.setLocation("New York");
    }

    @Test
    @Transactional
    void testCreateUser() {
        // Create the user using the service
        UserDto createdUserDto = userService.createUser(userDto);

        // Assert that the user was created
        assertNotNull(createdUserDto.getId());
        assertEquals("John", createdUserDto.getFname());
        assertEquals("Doe", createdUserDto.getLname());
        assertEquals("john.doe@example.com", createdUserDto.getEmail());
        assertTrue(passwordEncoder.matches("password123", createdUserDto.getPassword()));
    }

    @Test
    @Transactional
    void testFindAllUsers() {
        // Create a user first
        userService.createUser(userDto);

        // Fetch all users
        List<UserDto> users = userService.findAllUsers();

        // Assert that there is at least one user
        assertFalse(users.isEmpty());
        assertEquals(12, users.size());
        assertEquals("ifeoluwa", users.get(0).getFname());
    }

    @Test
    @Transactional
    void testFindUserById() {
        // Create a user
        UserDto createdUserDto = userService.createUser(userDto);

        // Fetch the user by ID
        UserDto foundUserDto = userService.findUserById(createdUserDto.getId());

        // Assert that the user was found
        assertNotNull(foundUserDto);
        assertEquals(createdUserDto.getId(), foundUserDto.getId());
    }

    @Test
    @Transactional
    void testFindUserByEmail() {
        // Create a user
        userService.createUser(userDto);

        // Fetch the user by email
        UserDto foundUserDto = userService.findUserByEmail("john.doe@example.com");

        // Assert that the user was found
        assertNotNull(foundUserDto);
        assertEquals("john.doe@example.com", foundUserDto.getEmail());
    }

    @Test
    @Transactional
    void testDeleteUser() {
        // Create a user
        UserDto createdUserDto = userService.createUser(userDto);

        // Delete the user
        userService.deleteUser(createdUserDto.getId());

        // Try to find the user, should throw an exception or return null
        assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(createdUserDto.getId()));
    }

    @Test
    @Transactional
    void testUpdateUser() {
        // Create a user
        UserDto createdUserDto = userService.createUser(userDto);

        // Modify the user details
        createdUserDto.setFname("Jane");
        createdUserDto.setLname("Smith");

        // Update the user
        userService.updateUser(createdUserDto.getId(), createdUserDto);

        // Fetch the updated user
        UserDto updatedUserDto = userService.findUserById(createdUserDto.getId());

        // Assert that the details were updated
        assertEquals("Jane", updatedUserDto.getFname());
        assertEquals("Smith", updatedUserDto.getLname());
    }

//    @Test
//    @Transactional
//    void testUpdateUserFields() {
//        // Create a user
//        UserDto createdUserDto = userService.createUser(userDto);
//
//        // Modify specific fields
//        createdUserDto.setEmail("new.email@example.com");
//        createdUserDto.setLocation("Los Angeles");
//
//        // Update specific fields
//        userService.updateUserFields(createdUserDto);
//
//        // Fetch the updated user
//        UserDto updatedUserDto = userService.findUserById(createdUserDto.getId());
//
//        // Assert that the fields were updated
//        assertEquals("new.email@example.com", updatedUserDto.getEmail());
//        assertEquals("Los Angeles", updatedUserDto.getLocation());
//    }

//    @Test
//    @Transactional
//    void testUpdatePasswords() {
//        // Create a user
//        userService.createUser(userDto);
//
//        // Update passwords (in the context of all users)
//        userService.updatePasswords();
//
//        // Fetch the user and assert that the password was encoded
//        User user = userRepository.findAll().get(0);
//        assertTrue(passwordEncoder.matches(userDto.getPassword(), user.getPassword()));
//    }
}
