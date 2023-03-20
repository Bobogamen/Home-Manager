package com.home_manager.service;

import com.home_manager.model.dto.YearDTO;
import com.home_manager.model.entities.Expense;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.Month;
import com.home_manager.model.entities.MonthHomes;
import com.home_manager.repository.MonthRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        newMonth.setCurrentIncome(0.00);
        newMonth.setTotalIncome(0.00);
        newMonth.setTotalExpenses(0.00);
        newMonth.setCurrentDifference(0.00);
        newMonth.setTotalDifference(0.00);
        newMonth.setPreviousMonthDifference(0.00);
        newMonth.setHomesGroup(homesGroup);

        return this.monthRepository.save(newMonth);
    }

    public void setHomesToMonth(Month newMonth, HomesGroup homesGroup) {

        newMonth.setHomes(homesGroup.getHomes().stream().map(h -> {

            MonthHomes monthHomes = new MonthHomes();
            monthHomes.setMonth(newMonth);
            monthHomes.setHome(h);
            monthHomes.setTotalPaid(0.00);
            monthHomes.setPaidDate(null);

            return monthHomes;
        }).toList());

        this.monthRepository.save(newMonth);
    }

    public MonthHomes getMonthHomeOfMonthById(Month month, long monthHomeId) {
        return month.getHomes().stream().filter(h -> h.getId() == monthHomeId).iterator().next();
    }

    public Month getMonthByNumberAndYearAndHomesGroupId(int month, int year, long homesGroupId) {
        return this.monthRepository.getMonthByNumberAndYearAndHomesGroupId(month, year, homesGroupId);
    }

    public void setTotalPaymentForHome(Month month, MonthHomes monthHome, double totalPaid, long homesGroupId) {

        monthHome.setTotalPaid(totalPaid);
        monthHome.setPaidDate(LocalDate.now());

        double monthCurrentIncome = getCurrentIncomeOfMonth(month);
        double monthTotalExpenses = getTotalExpensesOfMonth(month);

        double previousMonthDifference = getPreviousMonthDifference(month, homesGroupId);

        month.setCurrentIncome(monthCurrentIncome);
        month.setCurrentDifference(monthCurrentIncome - monthTotalExpenses);
        month.setTotalIncome(monthCurrentIncome + previousMonthDifference);
        month.setTotalDifference(month.getTotalIncome() - monthTotalExpenses);
        month.setPreviousMonthDifference(previousMonthDifference);

        this.monthRepository.save(month);
    }

    private double getCurrentIncomeOfMonth(Month month) {
        return month.getHomes().stream().mapToDouble(MonthHomes::getTotalPaid).sum();
    }
    private double getTotalExpensesOfMonth(Month month) {
        return month.getExpenses().stream().mapToDouble(Expense::getValue).sum();
    }

    public void calculateTotalExpense(Month currentMonth, long homesGroupId) {

        double totalExpenses = getTotalExpensesOfMonth(currentMonth);

        currentMonth.setTotalExpenses(totalExpenses);
        currentMonth.setCurrentDifference(currentMonth.getCurrentIncome() - totalExpenses);
        currentMonth.setTotalDifference(currentMonth.getTotalIncome() - totalExpenses);
        currentMonth.setPreviousMonthDifference(getPreviousMonthDifference(currentMonth, homesGroupId));

        this.monthRepository.save(currentMonth);
    }

    public YearDTO getYear(int yearNumber, long homesGroupId) {
        YearDTO year = new YearDTO();
        year.setMonths(this.monthRepository.getMonthsByYearAndHomesGroupId(yearNumber, homesGroupId));
        year.setNumber(yearNumber);

        return year;
    }

    public List<YearDTO> years (List<Integer> yearsList, long homesGroupId) {
        return yearsList.stream().map(y -> getYear(y, homesGroupId)).toList();
    }

    private double getPreviousMonthDifference(Month currentMonth, long homesGroupId) {

        int previousMonthNumber = currentMonth.getNumber() > 1 ? currentMonth.getNumber() - 1 : 12;
        int previousMonthYear = previousMonthNumber == 12 ? currentMonth.getYear() -1 : currentMonth.getYear();

        Month previousMonth = this.monthRepository.getMonthByNumberAndYearAndHomesGroupId(previousMonthNumber, previousMonthYear, homesGroupId);

        if (previousMonth != null) {
            return previousMonth.getTotalDifference();
        }

        return 0.00;
    }
}
