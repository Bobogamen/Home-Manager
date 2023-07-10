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
import java.util.*;

@Service
public class MonthService {

    private final MonthRepository monthRepository;

    public MonthService(MonthRepository monthRepository) {
        this.monthRepository = monthRepository;
    }

    public Month getMonthById(long monthId) {
        return this.monthRepository.getMonthById(monthId);
    }

    public Month createMonth(int month, int year, HomesGroup homesGroup, Month previousMonth) {
        Month newMonth = new Month();
        newMonth.setNumber(month);
        newMonth.setYear(year);
        newMonth.setCompleted(false);
        newMonth.setCurrentIncome(0.00);
        newMonth.setTotalExpenses(0.00);
        newMonth.setCurrentDifference(0.00);
        newMonth.setTotalDifference(0.00);
        newMonth.setPreviousMonthDifference(0.00);
        newMonth.setHomesGroup(homesGroup);
        newMonth.setPreviousMonth(previousMonth);

        return newMonth;
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

    public YearDTO getYear(int yearNumber, long homesGroupId) {
        YearDTO year = new YearDTO();
        year.setMonths(this.monthRepository.getMonthsByYearAndHomesGroupId(yearNumber, homesGroupId).
                stream().sorted(Comparator.comparingInt(Month::getNumber)).toList());
        year.setNumber(yearNumber);

        return year;
    }

    public List<YearDTO> years(List<Integer> yearsList, long homesGroupId) {
        return yearsList.stream().map(y -> getYear(y, homesGroupId)).toList();
    }

    public void completeMonth(Month month) {
        month.setCompleted(true);
        this.monthRepository.save(month);
    }

    public Months[] getMonthsValuesCurrentYear() {

        int monthValue = LocalDate.now().getMonthValue();
        Months[] months = new Months[monthValue];

        System.arraycopy(Months.values(), 0, months, 0, monthValue);

        return months;
    }

    public Months[] getMonthsValuesStartPeriodYear(int monthValue) {

        int arraySize = 12 - monthValue + 1;
        Months[] months = new Months[arraySize];

        System.arraycopy(Months.values(), monthValue - 1, months, 0, arraySize);

        return months;
    }

    public Months[] getMonthsList(int year, int currentYear, LocalDate homesGroupStartPeriod) {

        if (year == currentYear) {
            return getMonthsValuesCurrentYear();
        } else if (year == homesGroupStartPeriod.getYear()) {
            return getMonthsValuesStartPeriodYear(homesGroupStartPeriod.getMonthValue());
        } else {
            return Months.values();
        }
    }

    public void initialMonthsGeneration(HomesGroup homesGroup) {
        int homesGroupStartMonth = homesGroup.getStartPeriod().getMonthValue();
        int homesGroupStartYear = homesGroup.getStartPeriod().getYear();

        Month previousMonth = null;
        for (int year = homesGroupStartYear; year <= LocalDate.now().getYear(); year++) {

            List<Month> monthsList = new ArrayList<>();

            for (int month = year == homesGroupStartYear ? homesGroupStartMonth : 1;
                 month <= (year < LocalDate.now().getYear() ? 12 : LocalDate.now().getMonthValue()); month++) {

                monthsList.add(previousMonth = createMonth(month, year, homesGroup, previousMonth));
            }

            this.monthRepository.saveAll(monthsList);
            monthsList.clear();
        }
    }
    public Month getPreviousMonth(int month, int year, HomesGroup homesGroup) {
        if (month == 1) {
            month = 12;
            year--;
        } else {
            month--;
        }
        return getMonthByNumberAndYearAndHomesGroupId(month, year, homesGroup.getId());
    }
    private void calculateMonth(Month month) {
        double monthCurrentIncome = getCurrentIncomeOfMonth(month);
        double monthTotalExpenses = getTotalExpensesOfMonth(month);

        month.setCurrentIncome(monthCurrentIncome);
        month.setTotalExpenses(monthTotalExpenses);
        month.setCurrentDifference(monthCurrentIncome - monthTotalExpenses);

        if (month.getPreviousMonth() != null) {
            month.setPreviousMonthDifference(month.getPreviousMonth().getTotalDifference());
            month.setTotalDifference(month.getCurrentDifference() + month.getPreviousMonthDifference());

        } else {
            Month previousMonth = getPreviousMonth(month.getNumber(), month.getYear(), month.getHomesGroup());
            if (previousMonth == null) {
                month.setTotalDifference(month.getCurrentDifference());
            } else {
                month.setPreviousMonth(previousMonth);
                month.setPreviousMonthDifference(previousMonth.getTotalDifference());
                month.setTotalDifference(month.getCurrentDifference() + month.getPreviousMonthDifference());
            }
        }
    }

    private double getCurrentIncomeOfMonth(Month month) {
        return month.getHomes().stream().mapToDouble(MonthHomes::getTotalPaid).sum();
    }

    private double getTotalExpensesOfMonth(Month month) {
        return month.getExpenses().stream().mapToDouble(Expense::getValue).sum();
    }
}
