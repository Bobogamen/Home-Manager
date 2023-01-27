package com.home_manager.service;

import com.home_manager.model.entities.Fee;
import com.home_manager.model.entities.Home;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.repository.FeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeeService {

    private final FeeRepository feeRepository;

    public FeeService(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }


    public Fee addFee(String name, double value, HomesGroup homesGroup) {

        Fee fee = new Fee();
        fee.setName(name);
        fee.setValue(value);
        fee.setAddedOn(LocalDate.now());
        fee.setHomesGroup(homesGroup);

       return this.feeRepository.save(fee);
    }

    public Fee getFeeById(long feeId) {
        return this.feeRepository.getFeeById(feeId);
    }

    public Fee changeFee(long id, String name, double value) {
        Fee fee = this.feeRepository.getFeeById(id);

        boolean hasChanges = false;

        if (!fee.getName().equals(name)) {
            fee.setName(name);
            hasChanges = true;
        } else if (fee.getValue() != value) {
            fee.setValue(value);
            hasChanges = true;
        }

        if (hasChanges) {
            return this.feeRepository.save(fee);
        } else {
            return fee;
        }
    }

    public List<Home> getAllHomesByFeeId(long feeId) {
        Fee fee = this.feeRepository.getFeeById(feeId);

        return new ArrayList<>();

    }
}
