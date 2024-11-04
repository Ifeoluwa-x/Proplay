package com.soccerapp.app.security;

import com.soccerapp.app.models.Role;
import com.soccerapp.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor injection for MyUserRepository
    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user by username.
     *
     * @param username the username to search for
     * @return the UserDetails object
     * @throws UsernameNotFoundException if the username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<com.soccerapp.app.models.User> userOptional = userRepository.findByEmail(email);//.findByUsername(username);
        if (userOptional.isPresent()) {
            com.soccerapp.app.models.User user = userOptional.get();

            // Ensure roles are correctly cast to String[]
            List<Role> rolesList = user.getRoles(); // Assuming roles is List<Role>
            String[] rolesArray = rolesList.stream()
                    .map(Role::getName) // Assuming Role has a getName() method
                    .toArray(String[]::new);

            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(rolesArray)
                    .build();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}