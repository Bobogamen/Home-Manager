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
import java.time.LocalDate;
import java.util.List;

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

    public Role admin() {
        return this.roleRepository.getById(1);
    }
    public Role manager() {
        return this.roleRepository.getById(2);
    }

    public Role cashier() {
        return this.roleRepository.getById(3);
    }

    public void register(RegistrationDTO registrationDTO) throws ServletException {
        User newUser = new User();
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setName(registrationDTO.getName());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newUser.setRegisteredOn(LocalDate.now());

        if (this.userRepository.count() == 0) {
            newUser.setRole(admin());
        }

        newUser.setRole(manager());
        newUser.setRole(cashier());

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


    public User getUserById(long id) {
        return this.userRepository.getUserById(id);
    }

    public boolean isOwner(long homesGroupId, long userId) {
        return this.userRepository.getUserById(userId).getHomesGroup().stream().anyMatch(hg -> hg.getId() == homesGroupId);
    }

    public void registerCashier(RegistrationDTO registrationDTO, long id) {
        User cashier = new User();
        cashier.setName(registrationDTO.getName());
        cashier.setEmail(registrationDTO.getEmail());
        cashier.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        cashier.setRegisteredOn(LocalDate.now());
        cashier.setRole(cashier());

        User manager = getUserById(id);
        manager.setCashier(cashier);

        this.userRepository.saveAll(List.of(cashier, manager));
    }

    public void editUserName(long id, String name) {
        User user = getUserById(id);
        user.setName(name);
        this.userRepository.save(user);
    }

    public void changePasswordByUserId(long id, String password) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(password));

        this.userRepository.save(user);
    }
}
