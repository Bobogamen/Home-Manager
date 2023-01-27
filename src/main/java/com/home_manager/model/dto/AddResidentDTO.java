package com.home_manager.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AddResidentDTO {

    @NotEmpty(message = "{not_empty}")
    @Size(min = 2, max = 30, message = "{field_length}")
    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phoneNumber;

    public AddResidentDTO() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}