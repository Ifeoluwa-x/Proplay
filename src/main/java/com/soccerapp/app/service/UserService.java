package com.soccerapp.app.service;

import com.soccerapp.app.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    UserDto createUser(UserDto userDto);

    void deleteUser(Long id);

    UserDto findUserById(Long id);

    void updateUser(UserDto userDto);

    public void updateUserFields(UserDto userDto);
}
