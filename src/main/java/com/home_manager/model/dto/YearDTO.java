package com.home_manager.model.dto;

import com.home_manager.model.entities.Month;

import java.util.List;

public class YearDTO {

    private int number;
    private List<Month> months;

    public YearDTO() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Month> getMonths() {
        return months;
    }

    public void setMonths(List<Month> months) {
        this.months = months;
    }
}
