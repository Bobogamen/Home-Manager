package com.home_manager.model.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate registeredOn;

    @Column
    private String resetPasswordToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles")
    private List<Role> role;

    @ManyToMany(mappedBy = "users",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<HomesGroup> homesGroup;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<User> cashiers;

    public User() {
        this.role = new ArrayList<>();
        this.homesGroup = new ArrayList<>();
        this.cashiers = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRole() {
        return role.stream().toList();
    }

    public void setRole(Role role) {
        this.role.add(role);
    }

    public List<HomesGroup> getHomesGroup() {
        return homesGroup;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public void setHomesGroup(List<HomesGroup> homesGroup) {
        this.homesGroup = homesGroup;
    }

    public List<User> getCashiers() {
        return cashiers;
    }

    public void setCashier(User cashier) {
        this.cashiers.add(cashier);
    }

    public boolean hasThisHomesGroup(HomesGroup homesGroup) {
        return this.homesGroup.stream().anyMatch(hg -> hg.getId() == homesGroup.getId());
    }
}
