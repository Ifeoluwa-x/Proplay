package com.soccerapp.app.service;

import com.soccerapp.app.dto.UserDto;
import com.soccerapp.app.models.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    UserDto findUserByEmail(String email);

    UserDto createUser(UserDto userDto);

    void deleteUser(Long id);

    UserDto findUserById(Long id);

    public void updateUser(Long id, UserDto userDto);

    public void updateUserFields(UserDto userDto);

//    public void updatePasswords();

    public void toggleFname(String currentName, String newName);

    public User mapToUser(UserDto user);
}
