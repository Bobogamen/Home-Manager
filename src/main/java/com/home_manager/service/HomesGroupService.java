package com.home_manager.service;

import com.home_manager.model.dto.AddHomesGroupDTO;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.User;
import com.home_manager.repository.HomesGroupRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class HomesGroupService {

    private final HomesGroupRepository homesGroupRepository;

    public HomesGroupService(HomesGroupRepository homesGroupRepository) {
        this.homesGroupRepository = homesGroupRepository;
    }

    public void addHomesGroup(AddHomesGroupDTO addHomesGroupDTO, User user) {

        HomesGroup homesGroup = new HomesGroup();
        homesGroup.setName(addHomesGroupDTO.getName());
        homesGroup.setType(addHomesGroupDTO.getType());
        homesGroup.setSize(addHomesGroupDTO.getSize());
        homesGroup.setStartPeriod(addHomesGroupDTO.getStartPeriod());
        homesGroup.setBackgroundColor(addHomesGroupDTO.getBackgroundColor());
        homesGroup.addUser(user);

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
        homesGroup.setBackgroundColor(addHomesGroupDTO.getBackgroundColor());

        this.homesGroupRepository.save(homesGroup);
    }

    public void homesGroupAssignment(List<HomesGroup> managerHomesGroup, User cashier, List<Long> homesGroupsIds) {

        List<HomesGroup> updated = new ArrayList<>();

        if (homesGroupsIds != null) {

            managerHomesGroup.forEach(hg -> {
                boolean isFound = cashier.getHomesGroup().stream().anyMatch(uhg -> uhg.getId() == hg.getId());
                boolean isRequested = homesGroupsIds.stream().anyMatch(id -> id == hg.getId());

                if (isFound) {
                    if (!isRequested) {
                        hg.removeUser(cashier);
                        updated.add(hg);

                    }
                } else {
                    if (isRequested) {
                        hg.addUser(cashier);
                        updated.add(hg);
                    }
                }
            });
        } else  {
            managerHomesGroup.forEach(hg -> {
                hg.removeUser(cashier);
                updated.add(hg);
            });
        }
        this.homesGroupRepository.saveAll(updated);
    }
}
