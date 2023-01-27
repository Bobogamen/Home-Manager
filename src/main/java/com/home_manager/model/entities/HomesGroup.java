package com.home_manager.model.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "homes_group")
public class HomesGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    private int size;

    @OneToMany(mappedBy = "homesGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Home> homes;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "homesGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fee> fees;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> users;

    public HomesGroup() {
        this.homes = new HashSet<>();
        this.users = new HashSet<>();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Home> getHomes() {

        return this.homes.stream().sorted(Comparator.comparing(Home::getFloor).
                thenComparing((n1, n2) -> {
                    if(canConvertToInt(n1.getName()) && canConvertToInt(n2.getName())) {
                        return Integer.compare(Integer.parseInt(n1.getName()), Integer.parseInt(n2.getName()));
                    } else  {
                        return n1.getName().compareTo(n2.getName());
                    }
                })).toList();
    }

    public int getTotalResidents() {
        int residentsCount = 0;
        for (Home home : this.homes) {
            residentsCount += home.getResidents().size();
        }

        return residentsCount;
    }

    public void setHomes(Set<Home> homes) {
        this.homes = homes;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(User users) {
        this.users.add(users);
    }

    private static boolean canConvertToInt(String number) {
        try {
            Integer.parseInt(number);

            return true;
        } catch (NumberFormatException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public List<Fee> getFees() {
        return fees;
    }

    public void setFees(List<Fee> fees) {
        this.fees = fees;
    }
}
