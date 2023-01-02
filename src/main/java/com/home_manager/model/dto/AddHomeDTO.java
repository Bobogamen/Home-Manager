package com.home_manager.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AddHomeDTO {

    @NotEmpty(message = "{not_empty}")
    private String floor;

    @NotEmpty(message = "{not_empty}")
    private String name;

    @NotEmpty(message = "{not_empty}")
    @Size(min = 2, max = 30, message = "{field_length}")
    private String ownerFirstName;

    private String ownerMiddleName;

    private String ownerLastName;

    private String ownerEmail;

    private String ownerPhoneNumber;

    private boolean resident = true;

    @NotEmpty(message = "{not_empty}")
    @Size(min = 2, max = 30, message = "{field_length}")
    private String residentFirstName;

    private String residentMiddleName;

    private String residentLastName;

    private String residentEmail;

    private String residentPhoneNumber;

    public AddHomeDTO() {
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerMiddleName() {
        return ownerMiddleName;
    }

    public void setOwnerMiddleName(String ownerMiddleName) {
        this.ownerMiddleName = ownerMiddleName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public boolean isResident() {
        return resident;
    }

    public void setResident(boolean resident) {
        this.resident = resident;
    }

    public String getResidentFirstName() {
        return residentFirstName;
    }

    public void setResidentFirstName(String residentFirstName) {
        this.residentFirstName = residentFirstName;
    }

    public String getResidentMiddleName() {
        return residentMiddleName;
    }

    public void setResidentMiddleName(String residentMiddleName) {
        this.residentMiddleName = residentMiddleName;
    }

    public String getResidentLastName() {
        return residentLastName;
    }

    public void setResidentLastName(String residentLastName) {
        this.residentLastName = residentLastName;
    }

    public String getResidentEmail() {
        return residentEmail;
    }

    public void setResidentEmail(String residentEmail) {
        this.residentEmail = residentEmail;
    }

    public String getResidentPhoneNumber() {
        return residentPhoneNumber;
    }

    public void setResidentPhoneNumber(String residentPhoneNumber) {
        this.residentPhoneNumber = residentPhoneNumber;
    }
}