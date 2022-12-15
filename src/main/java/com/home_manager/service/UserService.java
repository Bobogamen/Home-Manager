package com.home_manager.service;

import com.home_manager.model.dto.RegistrationDTO;
import com.home_manager.model.entities.Role;
import com.home_manager.model.entities.User;
import com.home_manager.repository.RoleRepository;
import com.home_manager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final  HttpServletRequest httpServletRequest;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, HttpServletRequest httpServletRequest) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.httpServletRequest = httpServletRequest;
    }

    public void register(RegistrationDTO registrationDTO) throws ServletException {
        User newUser = new User();
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setFirstName(registrationDTO.getFirstName());
        newUser.setMiddleName(registrationDTO.getMiddleName());
        newUser.setLastName(registrationDTO.getLastName());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newUser.setRegisteredOn(LocalDateTime.now());

        Role manager = this.roleRepository.getById(2);
        Role cashier = this.roleRepository.getById(3);
        newUser.setRole(manager);
        newUser.setRole(cashier);

        this.userRepository.save(newUser);

        afterRegistrationLogin(httpServletRequest, registrationDTO.getEmail(), registrationDTO.getPassword());
    }

    public void afterRegistrationLogin(HttpServletRequest request, String username, String password) throws ServletException {
        request.login(username, password);
    }

    public boolean findEmail(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }

    public void updateResetPasswordToken(String token, String email) {
        User user = this.userRepository.getUserByEmail(email);
        user.setResetPasswordToken(token);
        this.userRepository.save(user);
    }

    public boolean findEmailByToken(String token) {
        return this.userRepository.findByResetPasswordToken(token).isPresent();
    }

    public void changePasswordOnEmailByToken(String token, String password) {
        User user = this.userRepository.getUserByResetPasswordToken(token);

        user.setPassword(passwordEncoder.encode(password));
        user.setResetPasswordToken(null);

        this.userRepository.save(user);
    }

    public String getResetPasswordTokenByEmail(String email) {
        return this.userRepository.getResetPasswordTokenByEmail(email);
    }
}
