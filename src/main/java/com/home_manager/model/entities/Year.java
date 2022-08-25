package com.home_manager.model.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "years")
public class Year {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int value;

    @OneToMany
    @JoinTable(name="year_months",
            joinColumns = @JoinColumn( name="year_id"),
            inverseJoinColumns = @JoinColumn( name="month_id"))
    private Set<Month> months;

    public Year() {
        this.months = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Set<Month> getMonths() {
        return months;
    }

    public void setMonths(Set<Month> months) {
        this.months = months;
    }
}
