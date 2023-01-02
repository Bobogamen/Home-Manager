package com.home_manager.model.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    private List<Home> homes;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public HomesGroup() {
        this.homes = new ArrayList<>();
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

    public void setHomes(List<Home> homes) {
        this.homes = homes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
