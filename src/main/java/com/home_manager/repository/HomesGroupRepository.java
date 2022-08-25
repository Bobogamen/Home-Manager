package com.home_manager.repository;

import com.home_manager.model.entities.HomesGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomesGroupRepository extends JpaRepository<HomesGroup, Long> {

}
