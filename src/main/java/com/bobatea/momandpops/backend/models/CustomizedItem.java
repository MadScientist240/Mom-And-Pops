package com.bobatea.momandpops.backend.models;

import java.util.ArrayList;

public class CustomizedItem {
    private ArrayList<String> selectedModifications;
    private double totalCost;
    private double baseCost;
    private String color = "";
    private String size = "";
    private String crust = "";
    private int quantity;
    public String name;
    private String imagePath;
    private Item originalItem;

    public CustomizedItem(String name, double baseCost, String imagePath, Item originalItem){
        this.name = name;
        this.quantity = 1;
        this.baseCost = baseCost;
        this.totalCost = baseCost;
        this.imagePath = imagePath;
        this.originalItem = originalItem;
        selectedModifications = new ArrayList<String>();
    }

    public void addModification(String modification, double charge){
        selectedModifications.add(modification);
        totalCost += charge;
    }

    public double getTotalCost(){
        return totalCost * quantity;
    }

    public ArrayList<String> getSelectedModifications() {
        return selectedModifications;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public Item getOriginalItem() {
        return originalItem;
    }
}
