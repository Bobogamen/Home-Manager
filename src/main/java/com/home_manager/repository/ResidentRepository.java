package com.home_manager.repository;


import com.home_manager.model.entities.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {

    Resident getResidentById(long id);
}
