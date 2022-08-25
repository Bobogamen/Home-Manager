package com.home_manager.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "month_homes")
public class MonthHomes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "month_id",
            referencedColumnName = "id",
            nullable = false)
    private Month month;

    @ManyToOne
    @JoinColumn(name = "home_id",
            referencedColumnName = "id",
            nullable = false)
    private Home home;

    @ManyToOne
    @JoinColumn(name = "resident_id",
            referencedColumnName = "id",
            nullable = false)
    private Resident resident;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double actualPayment = 0.0;

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

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public double getActualPayment() {
        return actualPayment;
    }

    public void setActualPayment(double actualPayment) {
        this.actualPayment = actualPayment;
    }
}
