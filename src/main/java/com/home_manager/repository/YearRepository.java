package com.home_manager.repository;

import com.home_manager.model.entities.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearRepository extends JpaRepository<Year, Long> {
}
