package com.home_manager.init;

import com.home_manager.model.entities.Role;
import com.home_manager.model.enums.RoleEnum;
import com.home_manager.repository.HomesGroupRepository;
import com.home_manager.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class Initializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final HomesGroupRepository homesGroupRepository;

    public Initializer(RoleRepository roleRepository, HomesGroupRepository homesGroupRepository) {
        this.roleRepository = roleRepository;
        this.homesGroupRepository = homesGroupRepository;
    }

    @Override
    public void run(String... args) {
        if (this.roleRepository.count() == 0) {
            List<Role> roles = Arrays.stream(RoleEnum.values()).map(Role::new).toList();

            this.roleRepository.saveAll(roles);
        }
    }
}
