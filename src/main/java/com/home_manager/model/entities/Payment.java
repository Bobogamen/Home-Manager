package com.home_manager.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Fee fee;

    @ManyToOne
    private MonthHomes monthHomes;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double valuePaid;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double feeValue;

    @Column(nullable = false)
    private int timesPaid;

    public Payment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public MonthHomes getMonthHomes() {
        return monthHomes;
    }

    public void setMonthHomes(MonthHomes monthHomes) {
        this.monthHomes = monthHomes;
    }

    public double getValuePaid() {
        return valuePaid;
    }

    public void setValuePaid(double valuePaid) {
        this.valuePaid = valuePaid;
    }

    public double getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(double feeValue) {
        this.feeValue = feeValue;
    }

    public int getTimesPaid() {
        return timesPaid;
    }

    public void setTimesPaid(int timesPaid) {
        this.timesPaid = timesPaid;
    }
}


