package com.home_manager.model.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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
    @Column(nullable = false)
    private String backgroundColor;
    private int size;
    @OneToMany(mappedBy = "homesGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Home> homes;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "homesGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fee> fees;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "homesGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Month> months;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> users;

    public HomesGroup() {
        this.homes = new HashSet<>();
        this.users = new HashSet<>();
        this.months = new ArrayList<>();
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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
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

    public void addUser(User users) {
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

    public void removeUser(User user) {
        this.users = this.users.stream()
                .filter(u -> u.getId() != user.getId())
                .collect(Collectors.toSet());
    }

    public Month getMonthByDate(int month, int year) {
        return this.getMonths().stream()
                .filter(m -> m.getNumber() == month && m.getYear() == year)
                .findFirst().orElse(null);
    }

    public List<Month> getMonths() {
        return months.stream().sorted(Comparator.comparing(Month::getYear)).toList();
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public List<Integer> getYears() {
        List<Integer> years = new ArrayList<>();

        this.months.forEach(m -> {
            if (!years.contains(m.getYear())) {
                years.add(m.getYear());
            }
        });

        return years;
    }

    public double getTotalForPay() {
       return this.homes.stream().mapToDouble(Home::getTotalForMonth).sum();
    }
}
