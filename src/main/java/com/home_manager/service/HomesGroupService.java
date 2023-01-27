package com.home_manager.service;

import com.home_manager.model.dto.AddHomesGroupDTO;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.User;
import com.home_manager.repository.HomesGroupRepository;
import org.springframework.stereotype.Service;


@Service
public class HomesGroupService {

    private final HomesGroupRepository homesGroupRepository;

    public HomesGroupService(HomesGroupRepository homesGroupRepository) {
        this.homesGroupRepository = homesGroupRepository;
    }


    public void addHomesGroup(AddHomesGroupDTO addHomesGroupDTO, User user) {

        HomesGroup homesGroup = new HomesGroup();
        homesGroup.setName(addHomesGroupDTO.getName());
        homesGroup.setSize(addHomesGroupDTO.getSize());
        homesGroup.setType(addHomesGroupDTO.getType());
        homesGroup.setUsers(user);

        this.homesGroupRepository.save(homesGroup);
    }

    public HomesGroup getHomesGroupById(long id) {
        return this.homesGroupRepository.getHomesGroupById(id);
    }

    public void editHomesGroup(AddHomesGroupDTO addHomesGroupDTO, long id) {
        HomesGroup homesGroup = this.homesGroupRepository.getHomesGroupById(id);

        homesGroup.setName(addHomesGroupDTO.getName());
        homesGroup.setType(addHomesGroupDTO.getType());
        homesGroup.setSize(addHomesGroupDTO.getSize());

        this.homesGroupRepository.save(homesGroup);
    }
}
