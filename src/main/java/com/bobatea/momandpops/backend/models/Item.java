package com.bobatea.momandpops.backend.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Item {
    public String name;
    private String description;
    private double basePrice;
    private String type;
    public boolean hasSizes;
    public boolean hasToppings;
    public boolean hasColors;
    public boolean hasCrust;
    private ArrayList<String> colors;
    private Map<String, Double> toppings;
    private Map<String, Double> sizes;
    private Map<String, Double> crusts;

    public Item(String name, String description, double startingPrice, boolean isMenuItem, boolean hasColors, boolean hasSizes, boolean hasToppings, boolean hasCrust){
        this.name = name;
        this.basePrice = startingPrice;
        this.description = description;

        if(isMenuItem){
            type = "MENU";
        } else {
            type = "MERCH";
        }

        this.hasColors = hasColors;
        this.hasToppings = hasToppings;
        this.hasSizes = hasSizes;
        this.hasCrust = hasCrust;

        colors = hasColors ? new ArrayList<String>() : null;
        sizes = hasSizes ? Map.of("Small",10.99, "Medium",12.99, "Large",14.99, "Extra Large",16.99) : null;
        toppings = hasToppings ? new HashMap<>() : null;
        crusts = hasCrust ? Map.of("Thin", 1.00, "Regular", 0.00, "Pan", 2.00) : null;

    }

    public void addTopping(String topping, double charge){
        toppings.put(topping, charge);
    }

    public void addColor(String color){
        colors.add(color);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors(){return colors;}
    public Map<String, Double> getToppings(){return toppings;}
    public Map<String, Double> getSizes(){return sizes;}
    public Map<String, Double> getCrusts(){return crusts;}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
