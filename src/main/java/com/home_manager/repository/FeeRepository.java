package com.home_manager.repository;

import com.home_manager.model.entities.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {

    Fee getFeeById(Long id);
}
