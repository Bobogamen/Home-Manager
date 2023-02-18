package com.home_manager.model.dto;

public class HomePaymentsDTO {

    private long id;
    private String name;
    private double value;
    private int times;
    private double total;

    public HomePaymentsDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal() {
        this.total = this.value * this.times;
    }
}
