package com.home_manager.repository;


import com.home_manager.model.entities.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthRepository extends JpaRepository<Month, Long> {
}
