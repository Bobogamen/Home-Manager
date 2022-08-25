package com.home_manager.model.entities;

import com.home_manager.model.enums.HomesGroupEnum;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "homes_group")
public class HomesGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HomesGroupEnum type;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns=@JoinColumn(name="home_id"))
    private Set<Home> homes;

    public HomesGroup() {
        this.homes = new HashSet<>();
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

    public HomesGroupEnum getType() {
        return type;
    }

    public void setType(HomesGroupEnum type) {
        this.type = type;
    }

    public Set<Home> getHomes() {
        return homes;
    }

    public void setHomes(Set<Home> homes) {
        this.homes = homes;
    }
}
