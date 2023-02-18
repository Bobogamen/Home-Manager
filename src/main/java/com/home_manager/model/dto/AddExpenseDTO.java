package com.home_manager.model.dto;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

public class AddExpenseDTO {

    private LocalDate addedOn;

    @NotEmpty(message = "{not_empty}")
    private String name;

    private double value;

    @NotEmpty(message = "{not_empty}")
    private String documentNumber;

    public AddExpenseDTO() {
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
