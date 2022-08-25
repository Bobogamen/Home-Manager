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


}
