package com.home_manager.repository;


import com.home_manager.model.entities.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayDeque;
import java.util.List;

@Repository
public interface MonthRepository extends JpaRepository<Month, Long> {

    Month getMonthById(long monthId);

    Month getMonthByNumberAndYearAndHomesGroupId(int number, int year, long homesGroup_id);

    List<Month> getMonthsByYearAndHomesGroupId(int year, long homesGroup_id);

    @Query("SELECT m FROM Month m WHERE m.year <= :year AND m.homesGroup.id = :homesGroupId ORDER BY m.number DESC, m.year")
    ArrayDeque<Month> findPreviousMonths(int year, long homesGroupId);


}
