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

    @OneToMany(mappedBy = "month",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<MonthHomes> homes;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name="month_expenses",
            joinColumns = @JoinColumn( name="month_id"),
            inverseJoinColumns = @JoinColumn( name="expense_id"))
    private Set<Expense> expenses;

    @Column(nullable = false,
            columnDefinition = "DECIMAL(10,2)")
    private double total;

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
}
