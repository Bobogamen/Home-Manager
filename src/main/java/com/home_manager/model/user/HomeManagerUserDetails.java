package com.home_manager.model.user;

import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class HomeManagerUserDetails implements UserDetails {

    private long id;
    private String username;
    private String name;
    private String password;
    private LocalDate registeredOn;
    private Collection<GrantedAuthority> authorities;
    private final List<HomesGroup> homesGroup;
    private final List<User> cashiers;

    public HomeManagerUserDetails(long id,
                                  String email,
                                  String name,
                                  String password,
                                  LocalDate registeredOn,
                                  Collection<GrantedAuthority> authorities,
                                  List<HomesGroup> homesGroup, List<User> cashiers) {
        this.id = id;
        this.username = email;
        this.name = name;
        this.password = password;
        this.registeredOn = registeredOn;
        this.authorities = authorities;
        this.homesGroup = homesGroup;
        this.cashiers = cashiers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return username;
    }

    public void setEmail(String email) {
        this.username = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public List<HomesGroup> getHomesGroups() {
        return homesGroup;
    }

    public void setHomesGroups(HomesGroup homesGroup) {
        this.homesGroup.add(homesGroup);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<HomesGroup> getHomesGroup() {
        return homesGroup;
    }

    public List<User> getCashiers() {
        return cashiers;
    }

    public boolean isCashier() {
        return authorities.stream().allMatch(r -> r.getAuthority().equals("ROLE_CASHIER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
