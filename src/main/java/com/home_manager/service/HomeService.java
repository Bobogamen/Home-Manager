package com.home_manager.service;

import com.home_manager.model.dto.AddHomeDTO;
import com.home_manager.model.entities.*;
import com.home_manager.repository.HomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        return this.homeRepository.getHomeById(id);
    }

    public void addResidentToHome(long id, Resident resident) {
        Home home = getHomeById(id);
        home.addResidents(resident);
        this.homeRepository.save(home);
    }

    public void setOwnerToHome(long id, Resident owner) {
        Home home = getHomeById(id);
        home.setOwner(owner);
        this.homeRepository.save(home);
    }

    public void setFeeToHome(long homeId, Fee fee) {
        Home home = getHomeById(homeId);

        boolean hasFee = home.getFees().stream().anyMatch(hf -> hf.getFee().getId() == fee.getId());

        if (!hasFee) {
            HomesFee homesFee = new HomesFee();
            homesFee.setFee(fee);
            homesFee.setTimes(1);
            homesFee.setHome(home);

            home.addFee(homesFee);
        }

        home.setTotalForMonth(calculateTotalForMonth(home));
        this.homeRepository.save(home);
    }

    public void unsetFeeToHome(long homeId, Fee fee) {
        Home home = getHomeById(homeId);

        List<HomesFee> feesList = home.getFees();

        if (feesList.size() > 0) {
            boolean hasFee = feesList.stream().anyMatch(hf -> hf.getFee().getId() == fee.getId());
            if (hasFee) {
                feesList = feesList.stream()
                        .filter(hf -> hf.getFee().getId() != fee.getId())
                        .toList();

                home.setFees(feesList);
            }

            home.setTotalForMonth(calculateTotalForMonth(home));
            this.homeRepository.save(home);
        }
    }

    public void changeTimesOfFeeById(long homeId, long feeId, int times) {

        Home home = getHomeById(homeId);

        home.getFees().stream().filter(f -> f.getId() == feeId).
                iterator().next().setTimes(times);

        home.setTotalForMonth(calculateTotalForMonth(home));

        this.homeRepository.save(home);
    }

    private double calculateTotalForMonth(Home home) {

        double totalForMonth = 0.0;

        for (HomesFee fee : home.getFees()) {
            totalForMonth += fee.getFee().getValue() * fee.getTimes();
        }

        return totalForMonth;
    }

    public void changeHomesFee(Map<Long, Boolean> homesMap, Fee fee) {
        for (Map.Entry<Long, Boolean> entry : homesMap.entrySet()) {
            long key = entry.getKey();
            boolean value = entry.getValue();
            if (value) {
                setFeeToHome(key, fee);
            } else {
                unsetFeeToHome(key, fee);
            }
        }
    }
}
