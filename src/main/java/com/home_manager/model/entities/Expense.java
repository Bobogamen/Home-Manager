package com.home_manager.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double value;

    @Column(nullable = false)
    private String receipt;

    private String utilityNumber;

    public Expense() {
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getUtilityNumber() {
        return utilityNumber;
    }

    public void setUtilityNumber(String utilityNumber) {
        this.utilityNumber = utilityNumber;
    }
}
