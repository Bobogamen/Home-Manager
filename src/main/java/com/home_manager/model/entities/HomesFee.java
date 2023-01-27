package com.home_manager.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "homes_fee")
public class HomesFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "home_id", referencedColumnName = "id")
    private Home home;

    @ManyToOne
    @JoinColumn(name = "fee_id", referencedColumnName = "id")
    private Fee fee;

    @Column(nullable = false)
    private int times;

    public HomesFee() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
