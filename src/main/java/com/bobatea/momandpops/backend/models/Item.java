package com.bobatea.momandpops.backend.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Item {
    public String name;
    private int itemId;
    private String description;
    private double basePrice;
    private String type;
    public boolean hasSizes = false;
    public boolean hasToppings = false;
    public boolean hasColors = false;
    public boolean hasCrust = false;
    public String imagePath;
    private ArrayList<String> colors = new ArrayList<String>();
    private HashMap<String, Double> toppings = new HashMap<String, Double>();
    private HashMap<String, Double> sizes = new HashMap<String, Double>();
    private HashMap<String, Double> crusts = new HashMap<String, Double>();

    public Item(String name, int itemId, String description, double startingPrice, boolean isMenuItem, boolean hasSizes, String imagePath){
        this.name = name;
        this.itemId = itemId;
        this.basePrice = startingPrice;
        this.description = description;
        this.hasSizes = hasSizes;
        this.imagePath = imagePath;

        // ALWAYS initialize HashMaps to avoid null pointer
        this.sizes = new HashMap<>();
        this.crusts = new HashMap<>();
        this.toppings = new HashMap<>();
        this.colors = new ArrayList<>();

        if(isMenuItem){
            type = "MENU";
            if (hasSizes) {
                sizes.putAll(Map.of("Small",0.00, "Medium",1.50, "Large",2.25, "Extra Large",3.75));
            }
            crusts.putAll(Map.of("Thin", 1.00, "Regular", 0.00, "Pan", 2.00));
            this.hasToppings = true;
            this.hasCrust = true;
        } else {
            type = "MERCH";
            this.hasColors = true;
        }
    }

    public void addTopping(String topping, double charge){
        toppings.put(topping, charge);
    }

    public void addColor(String color){
        colors.add(color);
    }

    public void addSize(String size, double cost){
        sizes.put(size, cost);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getItemId(){
        return itemId;
    }

    public void setItemId(int itemId){
        this.itemId = itemId;
    }

    public ArrayList<String> getColors(){return colors;}
    public Map<String, Double> getToppings(){return toppings;}
    public Map<String, Double> getSizes(){return sizes;}
    public Map<String, Double> getCrusts(){return crusts;}

    /*
     * Getter and setter for attributes that produce a single string
     * Used for database storage
     */
    public String getAttributeAsString(String attributeType){
        String result = "";
        switch (attributeType){
            case "colors":
                if(this.hasColors){
                    for(String color : colors) {
                        result += color + ":";
                    }
                } else {
                    return "";
                }
                break;

            case "sizes":
                if(this.hasSizes){
                    for(Map.Entry<String, Double> size: sizes.entrySet()) {
                        result += size.getKey() + " " + size.getValue() + ":";
                    }
                } else {
                    return "";
                }
                break;

            case "crusts":
                if(this.hasCrust){
                    for(Map.Entry<String, Double> crust: crusts.entrySet()) {
                        result += crust.getKey() + " " + crust.getValue() + ":";
                    }
                } else {
                    return "";
                }
                break;

            case "toppings":
                if(this.hasToppings){
                    for(Map.Entry<String, Double> topping: toppings.entrySet()) {
                        result += topping.getKey() + " " + topping.getValue() + ":";
                    }
                } else {
                    return "";
                }
                break;
        }
        return result;
    }

    public void setAttributeAsString(String attribute, String attributeType){
        String[] splitAttribute;
        switch (attributeType){
            case "colors":
                if(this.hasColors){
                    splitAttribute = attribute.split(":");
                    colors.addAll(Arrays.asList(splitAttribute));
                }
                break;

            case "sizes":
                if(this.hasSizes){
                    splitAttribute = attribute.split(":");
                    for(String size : splitAttribute){
                        String[] splitSizeandCharge = size.split(" ");
                        sizes.put(splitSizeandCharge[0], Double.parseDouble(splitSizeandCharge[1]));
                    }
                }
                break;

            case "crusts":
                if(this.hasCrust){
                    splitAttribute = attribute.split(":");
                    for(String crust : splitAttribute){
                        String[] splitCrustandCharge = crust.split(" ");
                        sizes.put(splitCrustandCharge[0], Double.parseDouble(splitCrustandCharge[1]));
                    }
                }
                break;

            case "toppings":
                if(this.hasToppings){
                    splitAttribute = attribute.split(":");
                    for(String topping : splitAttribute){
                        String[] splitToppingandCharge = topping.split(" ");
                        sizes.put(splitToppingandCharge[0], Double.parseDouble(splitToppingandCharge[1]));
                    }
                }
                break;
        }
    }

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
