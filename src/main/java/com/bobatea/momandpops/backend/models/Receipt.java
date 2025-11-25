package com.bobatea.momandpops.backend.models;

public class Receipt {
    private int id;
    private int orderId;
    private double total;
    private String date;

    public Receipt(int id, int orderId, double total, String date) {
        this.id = id;
        this.orderId = orderId;
        this.total = total;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
