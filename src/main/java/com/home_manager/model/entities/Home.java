package com.home_manager.model.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "home", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Resident> residents;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "home", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthHomes> months;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "home", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HomesFee> fees;

    @ManyToOne
    private HomesGroup homesGroup;

    public Home() {
        this.residents = new ArrayList<>();
        this.months = new ArrayList<>();
        this.fees = new ArrayList<>();
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

    public List<MonthHomes> getMonths() {
        return months;
    }

    public void setMonths(MonthHomes months) {
        this.months.add(months);
    }

    public double getTotalForMonth() {
        return this.totalForMonth;
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

    public void setResidents(List<Resident> residents) {
        this.residents = residents;
    }

    public void setMonths(List<MonthHomes> months) {
        this.months = months;
    }

    public List<HomesFee> getFees() {
        return fees.stream().toList();
    }

    public void addFee(HomesFee homesFee) {
        this.fees.add(homesFee);
    }

    public void setFees(List<HomesFee> fees) {
        this.fees.clear();
        this.fees = fees;
    }

    @Override
    public int compareTo(Home home) {
        return 0;
    }
}
