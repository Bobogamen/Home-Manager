package com.home_manager.model.dto;

import com.home_manager.model.validation.PasswordMatcher;
import com.home_manager.model.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@PasswordMatcher(password = "password",
        confirmPassword = "confirmPassword")
public class RegistrationDTO {

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Enter valid email address.")
    @UniqueEmail
    private String email;

    @NotEmpty(message = "Field cannot be empty")
    @Size(min = 2, max = 30, message = "Field must be between 2 and 30 character")
    private String firstName;

    @NotEmpty(message = "Field cannot be empty")
    @Size(min = 2, max = 30, message = "Field must be between 2 and 30 character")
    private String middleName;

    @NotEmpty(message = "Field cannot be empty")
    @Size(min = 2, max = 30, message = "Field must be between 2 and 30 character")
    private String lastName;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 symbols")
    private String password;

    private String confirmPassword;

    public RegistrationDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
