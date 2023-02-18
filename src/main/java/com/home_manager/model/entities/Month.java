package com.home_manager.model.entities;

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
    private double totalIncome;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double totalExpenses;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double difference;

    @Column(nullable = false)
    private int paidHomesCount;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getTotalIncome() {
        return this.homes.stream()
                .mapToDouble(h ->
                        h.getPayments().stream().mapToDouble(Payment::getValuePaid).sum()).sum();
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getPaidHomesCount() {
        return paidHomesCount;
    }

    public void setPaidHomesCount(int paidHomesCount) {
        this.paidHomesCount = paidHomesCount;
    }

    public double getTotalExpenses() {
        return this.expenses.stream().mapToDouble(Expense::getValue).sum();
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getDifference() {
        return getTotalIncome() - getTotalExpenses();
    }

    public void setDifference(double difference) {
        this.difference = difference;
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

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}
