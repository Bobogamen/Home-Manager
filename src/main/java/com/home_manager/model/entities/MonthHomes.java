package com.home_manager.model.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "month_homes")
public class MonthHomes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Month month;

    @ManyToOne(fetch = FetchType.LAZY)
    private Home home;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private LocalDate paidDate;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double paidAmount;

    @Column(nullable = false)
    private int residentsCount;

    public MonthHomes() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Home getHouse() {
        return home;
    }

    public void setHouse(Home home) {
        this.home = home;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getResidentsCount() {
        return residentsCount;
    }

    public void setResidentsCount(int residentsCount) {
        this.residentsCount = residentsCount;
    }
}
