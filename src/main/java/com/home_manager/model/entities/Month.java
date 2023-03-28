package com.home_manager.model.entities;

import com.home_manager.utility.MonthsUtility;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "months")
public class Month {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double currentIncome;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double totalIncome;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double totalExpenses;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double currentDifference;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double totalDifference;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double previousMonthDifference;

    @ManyToOne
    private HomesGroup homesGroup;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "month", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthHomes> homes;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "month", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses;

    public Month() {
        this.homes = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMonthName(int number) {
        return MonthsUtility.getMonthName(number);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(double currentIncome) {
        this.currentIncome = currentIncome;
    }

    public double getTotalIncome() {
        return this.totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getCurrentDifference() {
        return currentDifference;
    }

    public void setCurrentDifference(double currentDifference) {
        this.currentDifference = currentDifference;
    }

    public double getTotalDifference() {
        return totalDifference;
    }

    public void setTotalDifference(double totalDifference) {
        this.totalDifference = totalDifference;
    }

    public double getPreviousMonthDifference() {
        return previousMonthDifference;
    }

    public void setPreviousMonthDifference(double previousMonthDifference) {
        this.previousMonthDifference = previousMonthDifference;
    }

    public HomesGroup getHomesGroup() {
        return homesGroup;
    }

    public void setHomesGroup(HomesGroup homesGroup) {
        this.homesGroup = homesGroup;
    }

    public List<MonthHomes> getHomes() {
        return homes;
    }

    public void setHomes(List<MonthHomes> homes) {
        this.homes = homes;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Expense expenses) {
        this.expenses.add(expenses);
    }

    public MonthHomes getHomeById(long monthHomeId) {
        return this.homes.stream().filter(h -> h.getId() == monthHomeId).iterator().next();
    }
}
