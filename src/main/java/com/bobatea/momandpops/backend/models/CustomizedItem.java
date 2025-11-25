package com.bobatea.momandpops.backend.models;

import java.util.ArrayList;

public class CustomizedItem {
    private ArrayList<String> selectedToppings;
    private double totalCost;
    private String color;
    private String size;
    private String crust;
    private int quantity;
    public String name;

    public CustomizedItem(String name, double baseCost){
        this.name = name;
        this.quantity = 1;
        this.totalCost = baseCost;
        selectedToppings = new ArrayList<String>();
    }

    public void addSelectedTopping(String topping, double charge){
        selectedToppings.add(topping);
        totalCost += charge;
    }

    public double getTotalCost(){
        return totalCost * quantity;
    }

    public ArrayList<String> getSelectedToppings() {
        return selectedToppings;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size, double charge) {
        this.size = size;
        totalCost += charge;
    }

    public String getCrust() {
        return crust;
    }

    public void setCrust(String crust, double charge) {
        this.crust = crust;
        totalCost += charge;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
