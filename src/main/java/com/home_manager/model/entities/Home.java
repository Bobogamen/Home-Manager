package com.home_manager.model.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "homes")
public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int floor;

    @OneToOne(fetch = FetchType.EAGER,
            targetEntity = Resident.class)
    private Resident owner;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "home_residents",
            joinColumns = @JoinColumn (name = "home_id"),
            inverseJoinColumns = @JoinColumn (name ="resident_id"))
    private Set<Resident> residents;

    @OneToMany(mappedBy = "home",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<MonthHomes> months;

    @OneToOne(fetch = FetchType.EAGER)
    private Fee fee;

    @Column(nullable = false,
            columnDefinition = "DECIMAL(10,2)")
    private double totalForMonth;

    public Home() {
        this.residents = new HashSet<>();
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

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Resident getOwner() {
        return owner;
    }

    public void setOwner(Resident owner) {
        this.owner = owner;
    }

    public Set<Resident> getResidents() {
        return residents;
    }

    public void setResidents(Set<Resident> residents) {
        this.residents = residents;
    }

    public Set<MonthHomes> getMonths() {
        return months;
    }

    public void setMonths(Set<MonthHomes> months) {
        this.months = months;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public double getTotalForMonth() {
        return totalForMonth;
    }

    public void setTotalForMonth(double totalForMonth) {
        this.totalForMonth = totalForMonth;
    }
}
