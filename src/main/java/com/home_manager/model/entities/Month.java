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
    private String name;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double total;

    @Column(nullable = false)
    private int homesCount;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MonthHomes> getHomes() {
        return homes;
    }

    public void setHomes(Set<MonthHomes> houses) {
        this.homes = houses;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public int getHomesCount() {
        return homesCount;
    }

    public void setHomesCount(int homesCount) {
        this.homesCount = homesCount;
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
}
