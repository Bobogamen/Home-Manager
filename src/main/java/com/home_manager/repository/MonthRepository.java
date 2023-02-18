package com.home_manager.repository;


import com.home_manager.model.entities.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthRepository extends JpaRepository<Month, Long> {

    Month getMonthById(long monthId);

    Month getMonthByNumberAndYear(int month, int year);

    List<Month> getMonthByYear(int year);
}
