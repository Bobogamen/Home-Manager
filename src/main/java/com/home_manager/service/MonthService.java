package com.home_manager.service;

import com.home_manager.model.dto.YearDTO;
import com.home_manager.model.entities.Expense;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.Month;
import com.home_manager.model.entities.MonthHomes;
import com.home_manager.model.enums.Months;
import com.home_manager.repository.MonthRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
        newMonth.setCompleted(false);
        newMonth.setIncome(0.00);
        newMonth.setTotalExpenses(0.00);
        newMonth.setCurrentDifference(0.00);
        newMonth.setTotalDifference(0.00);
        newMonth.setPreviousMonthDifference(0.00);
        newMonth.setHomesGroup(homesGroup);

        newMonth.setPreviousMonth(findPreviousMonth(newMonth));

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

    public void setTotalPaymentForHome(Month month, MonthHomes monthHome, double totalPaid, LocalDate paidDate) {
        monthHome.setTotalPaid(totalPaid);
        monthHome.setPaidDate(Objects.requireNonNullElseGet(paidDate, LocalDate::now));

        calculateMonth(month);

        this.monthRepository.save(month);
    }

    public void calculateTotalExpense(Month month) {
        calculateMonth(month);

        this.monthRepository.save(month);
    }

    private void calculateMonth(Month month) {
        double monthCurrentIncome = getCurrentIncomeOfMonth(month);
        double monthTotalExpenses = getTotalExpensesOfMonth(month);

        month.setIncome(monthCurrentIncome);
        month.setTotalExpenses(monthTotalExpenses);
        month.setCurrentDifference(monthCurrentIncome - monthTotalExpenses);

        if (month.getPreviousMonth() != null) {
            month.setPreviousMonthDifference(month.getPreviousMonth().getTotalDifference());
            month.setTotalDifference(month.getCurrentDifference() + month.getPreviousMonthDifference());

        } else {
            Month previousMonth = findPreviousMonth(month);
            if (previousMonth == null) {
                month.setTotalDifference(month.getCurrentDifference());
            } else {
                month.setPreviousMonth(previousMonth);
                month.setPreviousMonthDifference(previousMonth.getTotalDifference());
                month.setTotalDifference(month.getCurrentDifference() + month.getPreviousMonthDifference());
            }
        }
    }

    private Month findPreviousMonth(Month month) {

        Month previousMonth = null;

        new ArrayDeque<>(12);
        ArrayDeque<Month> listPreviousMonths;
        listPreviousMonths = this.monthRepository.findPreviousMonths(month.getYear(), month.getHomesGroup().getId());

        while (!listPreviousMonths.isEmpty()) {

            Month currentMonth = listPreviousMonths.pop();
            if (currentMonth.getYear() == month.getYear()) {
                if (currentMonth.getNumber() < month.getNumber()) {
                    previousMonth = currentMonth;
                    break;
                }
            }

            return previousMonth;
        }

        return null;
    }

    private double getCurrentIncomeOfMonth(Month month) {
        return month.getHomes().stream().mapToDouble(MonthHomes::getTotalPaid).sum();
    }
    private double getTotalExpensesOfMonth(Month month) {
        return month.getExpenses().stream().mapToDouble(Expense::getValue).sum();
    }

    public YearDTO getYear(int yearNumber, long homesGroupId) {
        YearDTO year = new YearDTO();
        year.setMonths(this.monthRepository.getMonthsByYearAndHomesGroupId(yearNumber, homesGroupId).
                stream().sorted(Comparator.comparingInt(Month::getNumber)).toList());
        year.setNumber(yearNumber);

        return year;
    }

    public List<YearDTO> years (List<Integer> yearsList, long homesGroupId) {
        return yearsList.stream().map(y -> getYear(y, homesGroupId)).toList();
    }

    public void completeMonth(Month month) {
        month.setCompleted(true);
        this.monthRepository.save(month);
    }

    public Months[] getMonthsValues() {

        int monthValue = LocalDate.now().getMonthValue();
        Months[] months = new Months[monthValue];

        System.arraycopy(Months.values(), 0, months, 0, monthValue);

        return months;
    }
}
