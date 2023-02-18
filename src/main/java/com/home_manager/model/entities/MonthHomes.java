package com.home_manager.model.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "month_homes")
public class MonthHomes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Month month;

    @ManyToOne
    private Home home;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "monthHomes", fetch = FetchType.LAZY)
    private List<Payment> payments;

    private LocalDate paidDate;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double totalPaid;

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

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Payment payment) {
        this.payments.add(payment);
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }
}
