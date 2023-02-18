package com.home_manager.service;

import com.home_manager.model.dto.YearDTO;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.Month;
import com.home_manager.model.entities.MonthHomes;
import com.home_manager.repository.MonthRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MonthService {

    private final MonthRepository monthRepository;

    public MonthService(MonthRepository monthRepository) {
        this.monthRepository = monthRepository;
    }

    public Month getMonthById(long monthId) {
        return this.monthRepository.getMonthById(monthId);
    }

    public Month createMonth(int month, int year, HomesGroup homesGroup) {
        Month newMonth = new Month();
        newMonth.setNumber(month);
        newMonth.setYear(year);
        newMonth.setTotalIncome(0.00);
        newMonth.setTotalExpenses(0.00);
        newMonth.setDifference(0.00);
        newMonth.setPaidHomesCount(0);
        newMonth.setHomesGroup(homesGroup);

        return this.monthRepository.save(newMonth);
    }

    public Month setHomesToMonth(Month newMonth, HomesGroup homesGroup) {

        newMonth.setHomes(homesGroup.getHomes().stream().map(h -> {

            MonthHomes monthHomes = new MonthHomes();
            monthHomes.setMonth(newMonth);
            monthHomes.setHome(h);
            monthHomes.setTotalPaid(0.00);
            monthHomes.setPaidDate(null);

            return monthHomes;
        }).toList());

        return this.monthRepository.save(newMonth);
    }

    public MonthHomes getMonthHomeOfMonthById(Month month, long monthHomeId) {
        return month.getHomes().stream().filter(h -> h.getId() == monthHomeId).iterator().next();
    }

    public void setTotalPaymentForHome(int monthNumber, int year, long monthHomeId, double totalPaid) {
        Month month = this.monthRepository.getMonthByNumberAndYear(monthNumber, year);

        MonthHomes monthHomes = month.getHomes().stream().filter(h -> h.getId() == monthHomeId).iterator().next();
        monthHomes.setTotalPaid(totalPaid);
        monthHomes.setPaidDate(LocalDate.now());

        month.setTotalIncome(month.getTotalIncome());
        month.setDifference(month.getDifference());

        this.monthRepository.save(month);
    }

    public void calculateTotalExpense(Month currentMonth) {

        currentMonth.setTotalExpenses(currentMonth.getTotalExpenses());
        currentMonth.setDifference(currentMonth.getDifference());

        this.monthRepository.save(currentMonth);
    }

    public YearDTO getYear(int yearNumber) {
        YearDTO year = new YearDTO();
        year.setMonths(this.monthRepository.getMonthByYear(yearNumber));
        year.setNumber(yearNumber);

        return year;
    }
}
