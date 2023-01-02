package com.home_manager.service;

import com.home_manager.model.entities.Role;
import com.home_manager.model.entities.User;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class HomeManagerUserDetailsService implements UserDetailsService {

    private  final UserRepository userRepository;

    public HomeManagerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username).map(this::map).
                orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
    }

    private UserDetails map(User user) {
        return new HomeManagerUserDetails(user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                user.getRegisteredOn(),
                user.getRole().stream().map(this::map).toList(),
                user.getHomesGroup());
    }

    private GrantedAuthority map (Role role) {
        return new SimpleGrantedAuthority("ROLE_" + role.getName().name());
    }
}
