package com.home_manager.repository;


import com.home_manager.model.entities.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonthRepository extends JpaRepository<Month, Long> {

    Month getMonthById(long monthId);

    Month getMonthByNumberAndYearAndHomesGroupId(int number, int year, long homesGroup_id);

    List<Month> getMonthsByYearAndHomesGroupId(int year, long homesGroup_id);

}
