package com.home_manager.model.entities;


import com.home_manager.model.enums.HomesGroupEnum;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "homes_group")
public class HomesGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HomesGroupEnum name;

    public HomesGroup(HomesGroupEnum name) {
        this.name = name;
    }

    @OneToMany
    private Set<Home> homes;

}
