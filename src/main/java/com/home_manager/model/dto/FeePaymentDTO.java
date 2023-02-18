package com.home_manager.model.dto;

public class FeePaymentDTO {

    private long id;
    private double value;
    private int timesPaid;
    private double total;

    public FeePaymentDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getTimesPaid() {
        return timesPaid;
    }

    public void setTimesPaid(int timesPaid) {
        this.timesPaid = timesPaid;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
