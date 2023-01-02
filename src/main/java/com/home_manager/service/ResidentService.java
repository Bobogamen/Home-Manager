package com.home_manager.service;

import com.home_manager.model.dto.AddHomeDTO;
import com.home_manager.model.entities.Home;
import com.home_manager.model.entities.Resident;
import com.home_manager.repository.ResidentRepository;
import org.springframework.stereotype.Service;

@Service
public class ResidentService {

    private final ResidentRepository residentRepository;

    public ResidentService(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    public Resident addOwner(AddHomeDTO addHomeDTO) {

        Resident owner = new Resident();
        owner.setFirstName(addHomeDTO.getOwnerFirstName());
        owner.setMiddleName(addHomeDTO.getOwnerMiddleName());
        owner.setLastName(addHomeDTO.getOwnerLastName());
        owner.setEmail(addHomeDTO.getOwnerEmail());
        owner.setPhoneNumber(addHomeDTO.getOwnerPhoneNumber());

        return this.residentRepository.save(owner);
    }


    public void setResidentHome(Home home, Resident resident) {
        resident.setHome(home);

        this.residentRepository.save(resident);
    }

    public Resident addResident(AddHomeDTO addHomeDTO) {

        Resident resident = new Resident();
        resident.setFirstName(addHomeDTO.getResidentFirstName());
        resident.setMiddleName(addHomeDTO.getResidentMiddleName());
        resident.setLastName(addHomeDTO.getResidentLastName());
        resident.setEmail(addHomeDTO.getResidentEmail());
        resident.setPhoneNumber(addHomeDTO.getResidentPhoneNumber());

        return this.residentRepository.save(resident);
    }
}

