package com.bobatea.momandpops.backend.models;

import java.util.ArrayList;

public class CustomizedItem {
    private ArrayList<String> selectedToppings;
    public double totalCost;
    private String color;
    private String size;
    private String crust;
    public int quantity;
    public String name;

    public CustomizedItem(String name, int quantity, double baseCost){
        this.name = name;
        this.quantity = quantity;
        this.totalCost = baseCost;
    }

    public void addSelectedTopping(String topping, Double charge){
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

    public void setSize(String size) {
        this.size = size;
    }

    public String getCrust() {
        return crust;
    }

    public void setCrust(String crust) {
        this.crust = crust;
    }
}
