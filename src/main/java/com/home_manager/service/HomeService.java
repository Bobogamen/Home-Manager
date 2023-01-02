package com.home_manager.service;

import com.home_manager.model.dto.AddHomeDTO;
import com.home_manager.model.entities.Home;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.Resident;
import com.home_manager.repository.HomeRepository;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    private final HomeRepository homeRepository;

    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    public Home addHome(AddHomeDTO addHomeDTO, HomesGroup homeGroup) {

        Home home = new Home();
        home.setFloor(addHomeDTO.getFloor());
        home.setName(addHomeDTO.getName());
        home.setTotalForMonth(0);
        home.setHomesGroup(homeGroup);

        return this.homeRepository.save(home);
    }

    public Home getHomeById(long id) {
        return this.homeRepository.findById(id).get();
    }

    public void addResidentToHome(Home home, Resident resident) {
        home.addResidents(resident);
        this.homeRepository.save(home);
    }

    public void setOwnerToHome(Home home, Resident owner) {
        home.setOwner(owner);
        this.homeRepository.save(home);
    }
}
