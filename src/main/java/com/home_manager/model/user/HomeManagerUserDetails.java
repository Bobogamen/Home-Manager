package com.home_manager.model.user;

import com.home_manager.model.entities.HomesGroup;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class HomeManagerUserDetails implements UserDetails {

    private long id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;

    private Collection<GrantedAuthority> authorities;

    private Set<HomesGroup> homesGroups;

    public HomeManagerUserDetails(long id,
                                  String email,
                                  String firstName,
                                  String middleName,
                                  String lastName,
                                  String password,
                                  Collection<GrantedAuthority> authorities,
                                  Set<HomesGroup> homesGroups) {
        this.id = id;
        this.username = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.password = password;
        this.authorities = authorities;
        this.homesGroups = homesGroups;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Set<HomesGroup> getHomesGroups() {
        return homesGroups;
    }

    public void setHomesGroups(Set<HomesGroup> homesGroups) {
        this.homesGroups = homesGroups;
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
