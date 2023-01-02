package com.home_manager.model.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "homes")
public class Home implements Comparable<Home>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String floor;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double totalForMonth;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resident.class)
    private Resident owner;

    @OneToMany(mappedBy = "home", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Resident> residents;

    @OneToMany(mappedBy = "home", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MonthHomes> months;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<Fee> fee;

    @ManyToOne(fetch = FetchType.LAZY)
    private HomesGroup homesGroup;

    public Home() {
        this.residents = new ArrayList<>();
        this.months = new HashSet<>();
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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Resident getOwner() {
        return owner;
    }

    public void setOwner(Resident owner) {
        this.owner = owner;
    }

    public List<Resident> getResidents() {
        return this.residents;
    }

    public void addResidents(Resident resident) {
        this.residents.add(resident);
    }

    public Set<MonthHomes> getMonths() {
        return months;
    }

    public void setMonths(Set<MonthHomes> months) {
        this.months = months;
    }

    public double getTotalForMonth() {
        return totalForMonth;
    }

    public void setTotalForMonth(double totalForMonth) {
        this.totalForMonth = totalForMonth;
    }

    public HomesGroup getHomesGroup() {
        return homesGroup;
    }

    public void setHomesGroup(HomesGroup homesGroup) {
        this.homesGroup = homesGroup;
    }

    public Set<Fee> getFee() {
        return fee;
    }

    public void setFee(Set<Fee> fee) {
        this.fee = fee;
    }

    @Override
    public int compareTo(Home home) {
        return 0;
    }
}
