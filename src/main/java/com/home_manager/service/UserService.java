package com.home_manager.service;

import com.home_manager.model.dto.RegistrationDTO;
import com.home_manager.model.entities.User;
import com.home_manager.repository.RoleRepository;
import com.home_manager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void register(RegistrationDTO registrationDTO) {

        User newUser = new User();
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setFirstName(registrationDTO.getFirstName());
        newUser.setMiddleName(registrationDTO.getMiddleName());
        newUser.setLastName(registrationDTO.getLastName());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getEmail()));
        newUser.setRegisteredOn(LocalDateTime.now());

        this.userRepository.save(newUser);

        if (this.userRepository.count() == 0) {
            newUser.setRole(new HashSet<>(this.roleRepository.findAll()));

            this.userRepository.save(newUser);
        }
    }
}
