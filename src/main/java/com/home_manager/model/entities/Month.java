package com.home_manager.model.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "months")
public class Month {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double total;

    @Column(nullable = false)
    private int paidHomesCount;

    @Column(nullable = false)
    private int residentsCount;

    @OneToMany(mappedBy = "month", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MonthHomes> homes;

    @OneToMany(mappedBy = "month", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Expense> expenses;

    @ManyToOne(fetch = FetchType.LAZY)
    private Year year;

    public Month() {
        this.homes = new HashSet<>();
        this.expenses = new HashSet<>();
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getPaidHomesCount() {
        return paidHomesCount;
    }

    public void setPaidHomesCount(int paidHomesCount) {
        this.paidHomesCount = paidHomesCount;
    }

    public int getResidentsCount() {
        return residentsCount;
    }

    public void setResidentsCount(int residentsCount) {
        this.residentsCount = residentsCount;
    }

    public Set<MonthHomes> getHomes() {
        return homes;
    }

    public void setHomes(Set<MonthHomes> homes) {
        this.homes = homes;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
